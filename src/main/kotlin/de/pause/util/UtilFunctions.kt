package de.pause.util

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters


fun getWeekDatesFollowing(index: LocalDate): String {
    val nextMonday =
        index.with(TemporalAdjusters.previous(DayOfWeek.MONDAY)).format(DateTimeFormatter.ofPattern("dd.MM"))
    val nextFriday =
        index.with(TemporalAdjusters.next(DayOfWeek.FRIDAY)).format(DateTimeFormatter.ofPattern("dd.MM.Y"))
    return "${nextMonday} bis ${nextFriday}"
}