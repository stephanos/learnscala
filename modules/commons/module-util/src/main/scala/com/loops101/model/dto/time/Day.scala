package com.loops101.model.dto.time

import java.util._
import org.joda.time._
import com.loops101.util.TimeUtil

case class Day(year: Int, monthOfYear: Int, dayOfMonth: Int)
  extends Ordered[Day] {

  def +(operand: Int): Day =
    Day(this.toDateTime.plusDays(operand))

  def -(operand: Int): Day =
    this + (-operand)

  def -(operand: Day): Int =
    Days.daysBetween(operand.toDateTime, this.toDateTime).getDays

  def toDateTime =
    new DateTime(year, monthOfYear, dayOfMonth, 0, 0, 0, 0)

  def toDate =
    toDateTime.toDate

  def daysAgo =
    (TimeUtil.today - this).abs

  def toNum =
    TimeUtil.daysPassed(toDateTime)

  def name =
    toDateTime.dayOfWeek.getAsText(Locale.ENGLISH)

  override def toString =
    toDateTime.toString("y-MM-dd")

  def serialize =
    toDateTime.toString("yyyyMMdd")

  def compare(that: Day) = this.equals(that) match {
    case true => 0
    case false => (this.toDateTime) compareTo (that.toDateTime)
  }

}

object Day {

  //def apply(date: Date): Day =
  //  Day(date.getYear, date.getMonth + 1, date.getDay)

  def apply(date: DateTime): Day =
    Day(date.getYear, date.getMonthOfYear, date.getDayOfMonth)

  //def apply(str: String): Day =
  //  apply(new SimpleDateFormat("yyyyMMdd").parse(str))

  def latest(d1: Day, d2: Day) =
    if (d1 > d2) d1 else d2

}