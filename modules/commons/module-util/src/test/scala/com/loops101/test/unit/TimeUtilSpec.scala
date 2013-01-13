package com.loops101.test.unit

import java.util._
import org.joda.time.DateTime

import com.loops101.test.spec.UnitSpec
import com.loops101.util.TimeUtil
import com.loops101.model.dto.time.Day

class TimeUtilSpec
  extends UnitSpec {

  import TimeUtil._

  "Time Util" should {

    val d = new Date()
    val dt = new DateTime(d)
    val ms = d.getTime
    val s = (ms / 1000L).toInt
    val m = s / 60

    "return" >> {
      "today" >> {
        today === Day.today
      }
      "epoch" >> {
        epoch.getTime === 0
      }
      "milliseconds" >> {
        millis() must beCloseTo(ms, 10000)
      }
    }

    /*
    "calculate passed time" >> {
        "in seconds" >> {
            secsPassed() must beCloseTo(s, 10)
            secsPassed(d) must beCloseTo(1, 10)

            secsPassed(dt, dt) === 0
            secsPassed(dt, new DateTime) must beCloseTo(1, 10)
        }

        "in minutes" >> {
            val minAgo = dt.minusMinutes(1)

            minsPassed() must beCloseTo(m, 1)
            minsPassed(minAgo.toDate) must beCloseTo(1, 1)

            minsPassed(minAgo, minAgo) === 0
            minsPassed(minAgo, new DateTime) must beCloseTo(1, 1)
        }

        "in hours" >> {
            1 === 1
        }

        "in days" >> {
            1 === 1
        }
    }
    */
  }
}
