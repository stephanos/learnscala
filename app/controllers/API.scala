package controllers

import play.api.mvc._
import services.code.Compiler
import controllers.base.MyController

object API extends MyController {

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
