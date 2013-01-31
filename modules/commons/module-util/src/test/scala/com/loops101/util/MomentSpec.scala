package com.loops101.util

import com.loops101.test.spec.UnitSpec
import org.joda.time._

class MomentSpec
  extends UnitSpec {

  "Moment" should {

    import TimeUtil._

    val d0 = startOfTime
    val d1 = d0.plusMinutes(1)
    val d60 = d0.plusMinutes(60)

    "create" >> {
      "via absolute time" >> {
        "0" >> {
          val m = moment(d0)
          m.toDate === d0.toDate
          m.min === 0
        }

        "1" >> {
          val m = moment(d1)
          m.toDate === d1.toDate
          m.min === 1
        }

        "60" >> {
          val m = moment(d60)
          m.toDate === d60.toDate
          m.min === 60
        }
      }
      "via relative time" >> {
        val now = moment.min
        val hourAgo = ago(Hours.hours(1)).min
        now - hourAgo === 60
      }
    }

    "compare" >> {
      moment(d0) must beLessThan(moment(d1))
      moment(d60) must beGreaterThan(moment(d1))
    }
  }

}