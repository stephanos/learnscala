package com.loops101.model.dto.time

import com.loops101.util.TimeUtil
import TimeUtil._

import java.util.Date
import org.joda.time._

case class Moment(min: Int) // minutes since "start of time" (1.1.2012) - only 4 bytes
    extends Ordered[Moment] {

    lazy val getDate: Date =
        startOfTime.plusMinutes(min).toDate

    def compare(that: Moment) = this.equals(that) match {
        case true => 0
        case false => (this.min) compareTo (that.min)
    }
}

object Moment {

    def apply(): Moment =
        apply(new DateTime)

    def apply(d: DateTime): Moment =
        apply(minsPassed(startOfTime, d))

    def apply(d: Date): Moment =
        apply(new DateTime(d))


    def ago(millis: Long): Moment =
        apply(new DateTime(now.getTime - millis))

    def ago(hours: Hours): Moment =
        ago(hours.toStandardDuration.getMillis)
}
