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
                val r = Interpreter(code, None)
                val out = r._2
                if (r._1 == IR.Success)
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
                val r = Decoder(code)
                if (r._1 != null)
                    Ok(r._1)
                else
                    BadRequest(r._2)
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
                val r = Compiler(code)
                val out = r._2
                if (r._1 == true)
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
