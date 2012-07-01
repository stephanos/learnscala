package controllers

import play.api.mvc._
import services.code._
import controllers.base.MyController

object API extends MyController {

    def interpret = Action(parse.text) {
        req =>
            val code = req.body
            try {
                Interpreter(code)
                Ok("")
            } catch {
                case e =>
                    BadRequest("")
            }
    }

    def compile = Action(parse.text) {
        req =>
            val code = req.body
            try {
                Compiler(code)
                Ok("")
            } catch {
                case e =>
                    BadRequest("")
            }
    }
}
