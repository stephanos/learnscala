package de.learnscala.api

import impl._
import play.api.mvc._
import play.api.Play.current
import play.api.mvc.RequestHeader
import scala.tools.nsc.interpreter.IR
import play.api.libs.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import com.loops101.util.LogUtil

trait API {

    self: Controller with LogUtil =>

    protected def userIsAdmin(implicit req: RequestHeader): Boolean

    def hello = Action {
        withCORS(Ok("You should not be here."))
    }

    def execute = Action(parse.urlFormEncoded) {
        req =>
            runWithTimeout() {
                if (userIsAdmin(req)) {
                    val src = req.body("source").mkString("")
                    val call = req.body("call").mkString("")
                    //val session = req.queryString("session").headOption
                    try {
                        val (r, out) = Interpreter((src, call))
                        if (r == IR.Success)
                            withCORS(Ok(out))
                        else
                            BadRequest(out)
                    } catch {
                        case e: Throwable =>
                            log.warn(e, "error for API call 'execute'")
                            InternalServerError
                    }
                } else {
                    Unauthorized("Only the admin can execute code")
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
                            BadRequest(err)
                    } catch {
                        case e: Throwable =>
                            log.warn(e, "error for API call 'decompile'")
                            InternalServerError
                    }
                } else {
                    Unauthorized("Only the admin can decompile code")
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
                            BadRequest(out)
                    } catch {
                        case e: Throwable =>
                            log.warn(e, "error for API call 'compile'")
                            InternalServerError
                    }
                } else {
                    Unauthorized("Only the admin can compile code")
                }
            }
    }

    private def withCORS(r: SimpleResult[_]) =
        r.withHeaders(
            "Access-Control-Allow-Origin" -> "https://www.learnscala.de",
            "Access-Control-Allow-Methods" -> "POST, GET"
        )

    private def runWithTimeout(timeoutMs: Long = 5000)(f: => Result): Result = {
        val promise = Akka.future(f)
        Async {
            promise.orTimeout("Oops", timeoutMs).map {
                value =>
                    value.fold(
                        r => r,
                        timeout => InternalServerError("timeout")
                    )
            }
        }
    }
}

