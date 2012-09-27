package de.learnscala.api

import impl._
import play.api.mvc._
import play.api.mvc.RequestHeader
import scala.tools.nsc.interpreter.IR

trait API  {

    self: Controller =>

    protected def userIsAdmin(implicit req: RequestHeader): Boolean

    def hello = Action {
        Ok("You should not be here.")
    }

    def execute = Action(parse.urlFormEncoded) {
        req =>
            if (userIsAdmin(req)) {
                val src = req.body("source").mkString("")
                val call = req.body("call").mkString("")
                //val session = req.queryString("session").headOption
                try {
                    val (r, out) = Interpreter((src, call))
                    if (r == IR.Success)
                        Ok(out)
                    else
                        BadRequest(out)
                } catch {
                    case e: Throwable =>
                        //log.warn("error for API call 'execute'", e)
                        InternalServerError
                }
            } else {
                Unauthorized("Only the admin can execute code")
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
            if (userIsAdmin(req)) {
                val code = req.body("source").mkString("")
                try {
                    val (out, err) = (if(asJava) JDecoder(code) else Decoder(code))
                    if (out != null)
                        Ok(out)
                    else
                        BadRequest(err)
                } catch {
                    case e: Throwable =>
                        //log.warn("error for API call 'decompile'", e)
                        InternalServerError
                }
            } else {
                Unauthorized("Only the admin can decompile code")
            }
    }

    def compile = Action(parse.urlFormEncoded) {
        req =>
            if (userIsAdmin(req)) {
                val code = req.body("code").mkString("")
                try {
                    val (r, out) = Compiler(code)
                    if (r == true)
                        Ok(out)
                    else
                        BadRequest(out)
                } catch {
                    case e: Throwable =>
                        //log.warn("error for API call 'compile'", e)
                        InternalServerError
                }
            } else {
                Unauthorized("Only the admin can compile code")
            }
    }
}
