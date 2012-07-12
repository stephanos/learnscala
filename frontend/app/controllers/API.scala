package controllers

import play.api.mvc._
import services.code._
import controllers.base.MyController
import scala.tools.nsc.interpreter.IR

object API extends MyController {

    def interpret = Action(parse.urlFormEncoded) {
        req =>
            val code = req.body("code").mkString("")
            //val session = req.queryString("session").headOption
            try {
                val (r, out) = Interpreter(code, None)
                if (r == IR.Success)
                    Ok(out)
                else
                    BadRequest(out)
            } catch {
                case e =>
                    log.warn("error for API call 'interpret'", e)
                    InternalServerError
            }
    }

    def decompile = Action(parse.urlFormEncoded) {
        req =>
            val code = req.body("code").mkString("")
            try {
                val (out, err) = Decoder(code)
                if (out != null)
                    Ok(out)
                else
                    BadRequest(err)
            } catch {
                case e =>
                    log.warn("error for API call 'decompile'", e)
                    InternalServerError
            }
    }

    def compile = Action(parse.urlFormEncoded) {
        req =>
            val code = req.body("code").mkString("")
            try {
                val (r, out) = Compiler(code)
                if (r == true)
                    Ok(out)
                else
                    BadRequest(out)
            } catch {
                case e =>
                    log.warn("error for API call 'compile'", e)
                    InternalServerError
            }
    }
}
