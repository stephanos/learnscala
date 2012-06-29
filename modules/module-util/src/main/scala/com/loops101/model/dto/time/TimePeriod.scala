package com.loops101.model.dto.time

import java.util.Date
import org.joda.time._

case class TimePeriod(start: Day, end: Day) {

    require(start == end || start.getTime.isBefore(end.getTime),
        "date of 'start' [" + start + "] must be before date of 'end' [" + end + "]")

    def this() = this(null, null)

    def moveStart(days: Int) =
        TimePeriod(start + days, end)

    def contains(day: Day) =
        start == day || end == day || new Interval(start.getTime, end.getTime).contains(day.getTime)

    def dayCount() =
        Days.daysBetween(start.getTime, end.getTime).getDays

    def days(): List[Day] =
        (0 to dayCount).map(i => Day(start.getTime.plusDays(i))).toList
}

object ON {

    def apply(year: Int, monthOfYear: Int, dayOfMonth: Int): TimePeriod =
        apply(Day(year, monthOfYear, dayOfMonth))

    def apply(day: Day): TimePeriod =
        TimePeriod(day, day)
}

object BETWEEN {

    def apply(start: Day, end: Day): TimePeriod =
        TimePeriod(start, end)

    def apply(start: DateTime, end: DateTime): TimePeriod =
        apply(Day(start), Day(end))

    def apply(start: Date, end: Day): TimePeriod =
        apply(Day(start), end)

    def apply(start: Day, days: Int): TimePeriod = days match {
        case d if (d < 0) => apply(start + days, start)
        case d => apply(start, start + days)
    }

    def apply(start: DateTime, days: Int): TimePeriod =
        apply(Day(start), days)
}