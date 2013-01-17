package com.loops101.model.dto.time

import org.joda.time.DateTime

/**
 *
 */
case class TimeSnapshot(sec: Long,
                        min: Long,
                        hour: Long,
                        day: Long,
                        week: Long,
                        month: Long,
                        date: DateTime)