package ru.petkeeper.util.converters

import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


fun convertDateToLocalDateTime(date: Date): LocalDateTime {
    return Timestamp(date.time).toLocalDateTime()
}

fun convertDateTimeToDate(dateTime: LocalDateTime): Date {
    return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant())
}

