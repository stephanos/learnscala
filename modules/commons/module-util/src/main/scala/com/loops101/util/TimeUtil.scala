package com.loops101.util

import org.joda.time._
import com.loops101.model.dto.time.Day
import java.util._

class TimeUtil {

    def epoch =
        new Date(0)

    def millisOfDay(dt: DateTime = new DateTime) =
        dt.millisOfDay().get()

    def millis(d: Date = new Date) =
        d.getTime

    def today =
        Day.today

    def minSinceMidnight(dt: DateTime = new DateTime) =
        dt.minuteOfDay().get()

    def snapshot(d: Date = new Date()) = {
        val ms = d.getTime
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
        secsPassed(new DateTime(since), new DateTime())

    def secsPassed(since: DateTime, until: DateTime = new DateTime): Int =
        Seconds.secondsBetween(since, until).getSeconds


    def minsPassed(): Int =
        minsPassed(startOfTime)

    def minsPassed(since: Date): Int =
        minsPassed(new DateTime(since), new DateTime())

    def minsPassed(since: DateTime, until: DateTime = new DateTime): Int =
        Minutes.minutesBetween(since, until).getMinutes


    def hoursPassed(): Int =
        hoursPassed(startOfTime)

    def hoursPassed(since: Date): Int =
        hoursPassed(new DateTime(since))

    def hoursPassed(since: DateTime): Int =
        Hours.hoursBetween(since, new DateTime()).getHours


    def daysPassed(): Int =
        daysPassed(startOfTime)

    def daysPassed(since: Date): Int =
        daysPassed(new DateTime(since))

    def daysPassed(since: DateTime): Int =
        Days.daysBetween(since, new DateTime()).getDays


    // ==== CONVERT

    def minutesToMillis(min: Int) =
        Minutes.minutes(min).toStandardDuration.getMillis

    def hoursToMillis(hours: Int) =
        Hours.hours(hours).toStandardDuration.getMillis


    // ==== OTHER

    /**
     * Returns a list of dates (start date plus additional days)
     */
    def listOfDates(start: Date, plus: Int) =
        (0 to plus).toList.map(i => new DateTime(start).plus(Days.days(i).toPeriod))

    lazy val startOfTime =
        new DateTime(2012, 1, 1, 0, 0, 0, 0) // DO NOT CHANGE THIS! EVER!
}

object TimeUtil extends TimeUtil {

    val realStart = new Date
    val debugStart = SystemUtil.getProperty("debug.time") match {
        case None => 0L
        case Some(t) => t.toLong
    }

    def now = {
        val r = new Date
        debugStart match {
            case 0 =>
            case t =>
                val elapsed = r.getTime - realStart.getTime
                r.setTime(debugStart + elapsed)
        }
        r
    }
}

case class TimeSnapshot(sec: Long, min: Long, hour: Long, day: Long, week: Long, month: Long, date: Date)