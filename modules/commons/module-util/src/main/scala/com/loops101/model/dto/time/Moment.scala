package com.loops101.model.dto.time

import org.joda.time.{Hours, DateTime}
import com.loops101.util.TimeUtil
import java.util.Date

/**
 * Minutes since "start of time" (1.1.2013) - only 4 bytes
 */
case class Moment(min: Int)
  extends Ordered[Moment] {

  def toDate: Date =
    toDateTime.toDate

  def toDateTime: DateTime =
    TimeUtil.startOfTime.plusMinutes(min)

  def compare(that: Moment): Int =
    this.equals(that) match {
      case true => 0
      case false => (this.min) compareTo (that.min)
    }
}
