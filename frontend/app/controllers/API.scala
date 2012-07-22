package controllers

import play.api.mvc._
import services.code._
import controllers.base.MyController
import scala.tools.nsc.interpreter.IR

object API extends MyController {

    def execute = Action(parse.urlFormEncoded) {
        req =>
            if (userIsAdmin(req)) {
                val src = req.body("source").mkString("")
                val call = req.body("call").mkString("")
                //val session = req.queryString("session").headOption
                try {
                    val (r, out) = Interpreter((src, call), None)
                    if (r == IR.Success)
                        Ok(out)
                    else
                        BadRequest(out)
                } catch {
                    case e: Throwable =>
                        log.warn("error for API call 'execute'", e)
                        InternalServerError
                }
            } else {
                Unauthorized
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
                        log.warn("error for API call 'decompile'", e)
                        InternalServerError
                }
            } else {
                Unauthorized
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
                        log.warn("error for API call 'compile'", e)
                        InternalServerError
                }
            } else {
                Unauthorized
            }
    }
}
