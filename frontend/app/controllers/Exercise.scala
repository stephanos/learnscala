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
                jOk(
                    status.map {
                        s => ("user" -> s.user.value) ~ ("results" -> s.results.value)
                    }
                )

                /*
                def r(max: Int = 5) = scala.util.Random.nextInt(max)
                def res = (0 to 2 + r()).map(_ => r(2))
                jOk((0 to 12).map(i => ("user" -> i) ~ ("results" -> res.dropRight(r(1)))))
                */

                /*
                jOk(
                    List(
                        ("user" -> "1") ~ ("results" -> List(0, 0)),
                        ("user" -> "2") ~ ("results" -> List(1, 1, 1)),
                        ("user" -> "3") ~ ("results" -> List(0, 1, 1)),
                        ("user" -> "4") ~ ("results" -> List(2, 1, 1)),
                        ("user" -> "5") ~ ("results" -> List(1, 1, 2)),
                        ("user" -> "6") ~ ("results" -> List(0, 2, 2)),
                        ("user" -> "7") ~ ("results" -> List(0, 0, 0, 0)),
                        ("user" -> "8") ~ ("results" -> List(2, 1, 1)),
                        ("user" -> "9") ~ ("results" -> List(1, 1, 2)),
                        ("user" -> "10") ~ ("results" -> List(0, 2, 2)),
                        ("user" -> "11") ~ ("results" -> List(0, 2, 2)),
                        ("user" -> "12") ~ ("results" -> List(0, 2, 2))
                    )
                )
                */
            } else {
                Unauthorized("Only the admin can request the exercise status")
            }
    }

    def write(id: String) = Action(parse.xml) {
        implicit req =>
            val xml = req.body

            val exercise = (xml \ "@id").text
            val user = (xml \ "@user").text
            val addr = req.remoteAddress

            val status = StatusDoc.create
            status.user.set(if(user == null || user.isEmpty) addr else user)
            status.exercise.set(exercise)
            status.results.set((xml \\ "task").map(_.text.trim.toInt).toList)

            StatusRepo.write(status)
            Ok
    }

    /*<test id="S030" user="b8:f6:b1:17:c6:75">
        <task num="0">
            2
        </task><task num="1">
            1
        </task><task num="2">
            1
        </task>
        <start>
            1346760761441
        </start>
        <end>
            1346760761441
        </end>
    </test>*/
}
