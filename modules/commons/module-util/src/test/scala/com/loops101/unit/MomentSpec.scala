package com.loops101.unit

import com.loops101.UnitSpec
import com.loops101.model.dto.time._
import com.loops101.util._
import org.joda.time._

class MomentSpec extends UnitSpec {

    "Moment" should {

        val d0 = TimeUtil.startOfTime
        val d1 = d0.plusMinutes(1)
        val d60 = d0.plusMinutes(60)

        "create" >> {
            "via absolute time" >> {
                "0" >> {
                    val m = Moment(d0)
                    m.getDate === d0.toDate
                    m.min === 0
                }

                "1" >> {
                    val m = Moment(d1)
                    m.getDate === d1.toDate
                    m.min === 1
                }

                "60" >> {
                    val m = Moment(d60)
                    m.getDate === d60.toDate
                    m.min === 60
                }
            }
            "via relative time" >> {
                val now = Moment().min
                val hourAgo = Moment.ago(Hours.hours(1)).min
                now - hourAgo === 60
            }
        }

        "compare" >> {
            Moment(d0) must beLessThan(Moment(d1))
            Moment(d60) must beGreaterThan(Moment(d1))
        }
    }
}