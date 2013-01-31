package com.loops101.util

import org.joda.time._
import com.loops101.model.dto.time.{TimeSnapshot, Day, Moment}
import java.util._
import java.text.SimpleDateFormat

object TimeUtil extends TimeUtilImpl

trait TimeUtil {

  lazy val timeUtil = new TimeUtilImpl
}

private[util] class TimeUtilImpl {

  @inline
  def now = {
    val r = new DateTime
    /*
    debugStart match {
        case 0 =>
        case t =>
            val elapsed = r.getTime - realStart.getTime
            r.setTime(debugStart + elapsed)
    }
    */
    r
  }

  /*
    val realStart = new Date
    val debugStart = sysUtil.getProperty("debug.time") match {
        case None => 0L
        case Some(t) => t.toLong
    }
    */

  def epoch =
    new DateTime(0)

  def millisOfDay(dt: DateTime = now) =
    dt.millisOfDay().get()

  def millis =
    now.getMillis

  def minSinceMidnight(dt: DateTime = now) =
    dt.minuteOfDay().get()

  def snapshot(d: DateTime = now) = {
    val ms = d.getMillis
    val sec = ms / 1000
    val min = sec / 60
    val hour = min / 60
    val day = hour / 24
    val week = day / 7
    val month = Months.monthsBetween(new MutableDateTime(0), new DateTime(d)).getMonths
    TimeSnapshot(sec, min, hour, day, week, month, d)
  }

  // ==== TIMEZONE

  def pacificOffset(d: Date) =
    new DateTime(d).withZone(DateTimeZone.forID("PST")).getMillisOfDay / 60000

  /*
  def timezoneOffset(tz: String) =
      try {
          Some(DateTimeZone.forID(tz).getOffset(null) / 60000)
      } catch {
          case e => None
      }
      */

  // ==== PASSED TIME

  def secsPassed(): Int =
    secsPassed(startOfTime)

  def secsPassed(since: Date): Int =
    secsPassed(new DateTime(since), now)

  def secsPassed(since: DateTime, until: DateTime = now): Int =
    Seconds.secondsBetween(since, until).getSeconds


  def minsPassed(): Int =
    minsPassed(startOfTime)

  def minsPassed(since: Date): Int =
    minsPassed(new DateTime(since), now)

  def minsPassed(since: DateTime, until: DateTime = now): Int =
    Minutes.minutesBetween(since, until).getMinutes


  def hoursPassed(): Int =
    hoursPassed(startOfTime)

  def hoursPassed(since: Date): Int =
    hoursPassed(new DateTime(since))

  def hoursPassed(since: DateTime): Int =
    Hours.hoursBetween(since, now).getHours


  def daysPassed(): Int =
    daysPassed(startOfTime)

  def daysPassed(since: Date): Int =
    daysPassed(new DateTime(since))

  def daysPassed(since: DateTime): Int =
    Days.daysBetween(since, now).getDays


  // ==== CONVERT

  def minutesToMillis(min: Int) =
    Minutes.minutes(min).toStandardDuration.getMillis

  def hoursToMillis(hours: Int) =
    Hours.hours(hours).toStandardDuration.getMillis


  // ==== MOMENT

  def moment: Moment =
    moment(new DateTime)

  def moment(d: DateTime): Moment =
    Moment(minsPassed(startOfTime, d))

  def moment(d: Date): Moment =
    moment(new DateTime(d))

  def ago(millis: Long): Moment =
    moment(new DateTime(now.getMillis - millis))

  def ago(hours: Hours): Moment =
    ago(hours.toStandardDuration.getMillis)


  // ==== DAY

  def yesterday() =
    today - 1

  def twoDaysAgo() =
    today - 2

  def tomorrow() =
    today + 1

  def today =
    Day(now)

  def isToday(d: Day) =
    today == d

  def isYesterday(d: Day) =
    yesterday == d

  def day(num: Int): Day =
    Day(startOfTime) + num

  def day(date: Date): Day = date match {
    case null => null // necesary 'cause could be used from MongoDB where null is possible
    case _ => Day(new DateTime(date))
  }

  /* def apply(opt: Option[Date]): Option[Day] = opt match {
      case None => None
      case Some(date) => Some(apply(date))
  }*/

  def day(str: String): Day =
    day(new SimpleDateFormat("yyyyMMdd").parse(str))

  def latest(d1: Day, d2: Day) =
    if (d1 > d2) d1 else d2


  // ==== OTHER

  /**
   * Returns a list of dates (start date plus additional days)
   */
  def listOfDates(start: Date, plus: Int) =
    (0 to plus).toList.map(i => new DateTime(start).plus(Days.days(i).toPeriod))

  lazy val startOfTime =
    new DateTime(2013, 1, 1, 0, 0, 0, 0) // DO NOT CHANGE THIS! EVER!
}