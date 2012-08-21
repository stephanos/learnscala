package controllers

import play.api.mvc._
import play.api.libs.json.Json._

import controllers.base.MyController
import services.data.model.{StatusDoc}
import services.data.dao.StatusRepo

object Exercise extends MyController {

    def read(id: String) = Action {
        implicit req =>
            if (userIsAdmin(req)) {
                val status = StatusRepo.read(id)
                Ok(toJson(
                    status.map {
                        s =>
                            Map("user" -> s.user.value,
                                "fail" -> s.fail.value.toString, "skip" -> s.skip.value.toString,
                                "success" -> s.success.value.toString, "error" -> s.error.value.toString,
                                "pending" -> s.pending.value.toString)
                    }
                ))
            } else {
                Unauthorized("Only the admin can request the exercise status")
            }
    }

    def write(id: String) = Action(parse.xml) {
        implicit req =>
            val xml = req.body
            val exercise = (xml \ "@name").text
            val addr = req.remoteAddress
            val status = StatusDoc.create
            status.user.set(addr)
            status.exercise.set(exercise)
            status.fail.set((xml \\ "fail").text.toInt)
            status.success.set((xml \\ "success").text.toInt)
            status.pending.set((xml \\ "pending").text.toInt)
            status.error.set((xml \\ "error").text.toInt)
            status.skip.set((xml \\ "skip").text.toInt)
            StatusRepo.write(status)
            Ok
    }
}
