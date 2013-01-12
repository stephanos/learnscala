package de.learnscala.api

import impl._
import play.api.mvc._
import play.api.Play.current
import play.api.mvc.RequestHeader
import scala.tools.nsc.interpreter.IR
import play.api.libs.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import com.loops101.util.{EnvUtil, LogUtil}
import play.api.http.HeaderNames

trait API {

    self: Controller with LogUtil =>

    protected def userIsAdmin(implicit req: RequestHeader): Boolean

    def hello = Action {
        withCORS(Ok("You should not be here."))
    }

    def execute = Action(parse.urlFormEncoded) {
        req =>
            runWithTimeout() {
                //println("EXECUTE")
                if (userIsAdmin(req)) {
                    val src = req.body("source").mkString("")
                    val call = req.body("call").mkString("")
                    //val session = req.queryString("session").headOption
                    try {
                        val (r, out) = Interpreter((src, call))
                        if (r == IR.Success)
                            withCORS(Ok(out))
                        else {
                            log.warn(out, "invalid request")
                            withCORS(BadRequest(out))
                        }
                    } catch {
                        case e: Throwable =>
                            log.warn(e, "error for API call 'execute'")
                            withCORS(InternalServerError)
                    }
                } else {
                    withCORS(Unauthorized("only the admin can execute code"))
                }
            }
    }

    def decompileAsJava = {
        decompile(asJava = true)
    }

    def decompileAsScala = {
        decompile(asJava = false)
    }

    private def decompile(asJava: Boolean) = Action(parse.urlFormEncoded) {
        req =>
            runWithTimeout() {
                if (userIsAdmin(req)) {
                    val code = req.body("source").mkString("")
                    try {
                        val (out, err) = (if (asJava) JDecoder(code) else Decoder(code))
                        if (out != null)
                            withCORS(Ok(out))
                        else
                            withCORS(BadRequest(err))
                    } catch {
                        case e: Throwable =>
                            log.warn(e, "error for API call 'decompile'")
                            withCORS(InternalServerError)
                    }
                } else {
                  withCORS(Unauthorized("only the admin can decompile code"))
                }
            }
    }

    def compile = Action(parse.urlFormEncoded) {
        req =>
            runWithTimeout() {
                if (userIsAdmin(req)) {
                    val code = req.body("code").mkString("")
                    try {
                        val (r, out) = Compiler(code)
                        if (r == true)
                            withCORS(Ok(out))
                        else
                            withCORS(BadRequest(out))
                    } catch {
                        case e: Throwable =>
                            log.warn(e, "error for API call 'compile'")
                            withCORS(InternalServerError)
                    }
                } else {
                    withCORS(Unauthorized("Only the admin can compile code"))
                }
            }
    }

    private def withCORS(r: SimpleResult[_]) =
        r.withHeaders(
            HeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN ->
                (if(EnvUtil.isCloud)
                    "https://www.learnscala.de"
                else
                    "*"),
            HeaderNames.ACCESS_CONTROL_ALLOW_METHODS -> "POST, GET"
        )

    private def runWithTimeout(timeoutMs: Long = 5000)(f: => Result): Result = {
        val promise = Akka.future(f)
        Async {
            promise.orTimeout("Oops", timeoutMs).map {
                value =>
                    value.fold(
                        r => r,
                        timeout => withCORS(InternalServerError("timeout"))
                    )
            }
        }
    }
}

