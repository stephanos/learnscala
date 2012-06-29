package com.loops101.model.dto.time

import java.util._
import org.joda.time._
import java.text.SimpleDateFormat
import com.loops101.util.TimeUtil

case class Day(year: Int, monthOfYear: Int, dayOfMonth: Int)
    extends Ordered[Day] {

    def this() =
        this(2010, 1, 1)

    def +(operand: Int): Day =
        Day(getTime.plusDays(operand))

    def -(operand: Int): Day =
        this + (-operand)

    def -(operand: Day): Int =
        Days.daysBetween(operand.getTime, getTime).getDays

    def getTime =
        new DateTime(year, monthOfYear, dayOfMonth, 0, 0, 0, 0)

    def getDate =
        getTime.toDate

    def daysAgo() =
        (Day.today - this).abs

    def isToday =
        Day.today == this

    def isYesterday =
        Day.yesterday == this

    def toNum =
        TimeUtil.daysPassed(getDate)

    def name() =
        getTime.dayOfWeek.getAsText(Locale.ENGLISH)

    override def toString =
        getTime.toString("y-MM-dd")

    def serialize() =
        getTime.toString("yyyyMMdd")

    //def getNiceString =
    //    getTime.toString("") // like 25 July

    def compare(that: Day) = this.equals(that) match {
        case true => 0
        case false => (this.getTime) compareTo (that.getTime)
    }
}

object Day {

    def today =
        apply()

    def yesterday() =
        today - 1

    def twoDaysAgo() =
        today - 2

    def tomorrow() =
        today + 1

    def apply(): Day =
        apply(TimeUtil.now)

    def apply(num: Int): Day =
        new Day() + num

    def apply(date: DateTime): Day =
        Day(date.getYear, date.getMonthOfYear, date.getDayOfMonth)

    def apply(date: Date): Day = date match {
        case null => null // necesary 'cause could be used from MongoDB where null is possible
        case _ => apply(new DateTime(date))
    }

    /* def apply(opt: Option[Date]): Option[Day] = opt match {
        case None => None
        case Some(date) => Some(apply(date))
    }*/

    def apply(str: String): Day =
        apply(new SimpleDateFormat("yyyyMMdd").parse(str))

    def latest(d1: Day, d2: Day) =
        if (d1 > d2) d1 else d2

}