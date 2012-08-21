package controllers

import play.api.mvc._

import controllers.base.MyController
import services.data.model.{StatusDoc}
import services.data.dao.StatusRepo

object Exercise extends MyController {

    def read(id: String) = Action {
        implicit req =>
            if (userIsAdmin(req)) {
                val status = StatusRepo.read(id)
                import net.liftweb.json.JsonDSL._
                /*
                jOk(
                    status.map {
                        s =>
                            ("user" -> s.user.value) ~
                                ("fail" -> s.fail.value.toString) ~ ("skip" -> s.skip.value.toString)~
                                ("success" -> s.success.value.toString) ~ ("error" -> s.error.value.toString)~
                                ("pending" -> s.pending.value.toString)
                    }
                )
                */
                def r = scala.util.Random.nextInt(5)
                jOk(
                List(
                    ("user" -> "1") ~ ("fail" -> r) ~ ("skip" -> r) ~ ("success" -> r) ~ ("error" -> r) ~ ("pending" -> r),
                    ("user" -> "2") ~ ("fail" -> r) ~ ("skip" -> r) ~ ("success" -> r) ~ ("error" -> r) ~ ("pending" -> r),
                    ("user" -> "3") ~ ("fail" -> r) ~ ("skip" -> r) ~ ("success" -> r) ~ ("error" -> r) ~ ("pending" -> r),
                    ("user" -> "4") ~ ("fail" -> r) ~ ("skip" -> r) ~ ("success" -> r) ~ ("error" -> r) ~ ("pending" -> r),
                    ("user" -> "5") ~ ("fail" -> r) ~ ("skip" -> r) ~ ("success" -> r) ~ ("error" -> r) ~ ("pending" -> r),
                    ("user" -> "6") ~ ("fail" -> r) ~ ("skip" -> r) ~ ("success" -> r) ~ ("error" -> r) ~ ("pending" -> r),
                    ("user" -> "7") ~ ("fail" -> r) ~ ("skip" -> r) ~ ("success" -> r) ~ ("error" -> r) ~ ("pending" -> r),
                    ("user" -> "8") ~ ("fail" -> r) ~ ("skip" -> r) ~ ("success" -> r) ~ ("error" -> r) ~ ("pending" -> r),
                    ("user" -> "9") ~ ("fail" -> r) ~ ("skip" -> r) ~ ("success" -> r) ~ ("error" -> r) ~ ("pending" -> r),
                    ("user" -> "10") ~ ("fail" -> r) ~ ("skip" -> r) ~ ("success" -> r) ~ ("error" -> r) ~ ("pending" -> r),
                    ("user" -> "11") ~ ("fail" -> r) ~ ("skip" -> r) ~ ("success" -> r) ~ ("error" -> r) ~ ("pending" -> r),
                    ("user" -> "12") ~ ("fail" -> r) ~ ("skip" -> r) ~ ("success" -> r) ~ ("error" -> r) ~ ("pending" -> r)
                ).take(12)
                )
            } else {
                Unauthorized("Only the admin can request the exercise status")
            }
    }

    def write(id: String) = Action(parse.xml) {
        implicit req =>
            val xml = req.body

            val exercise = (xml \ "@id").text
            val user = (xml \ "@user").text
            //val addr = req.remoteAddress

            val status = StatusDoc.create
            status.user.set(user)
            status.exercise.set(exercise)
            status.fail.set((xml \\ "fail").text.trim.toInt)
            status.success.set((xml \\ "success").text.trim.toInt)
            status.pending.set((xml \\ "pending").text.trim.toInt)
            status.error.set((xml \\ "error").text.trim.toInt)
            status.skip.set((xml \\ "skip").text.trim.toInt)
            StatusRepo.write(status)
            Ok
    }
}
