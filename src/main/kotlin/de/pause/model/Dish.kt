package de.pause.model

import kotlinx.serialization.Serializable

@Serializable
data class Dish(
    val description: String,
    val available: Boolean,
    val scheduled: String,
    val price: Double,
) {
    val order: Int
        get() = scheduled.dayOfWeekToInt()
}

fun String.dayOfWeekToInt(): Int = when (this) {
    "Montag" -> 1
    "Dienstag" -> 2
    "Mittwoch" -> 3
    "Donnerstag" -> 4
    "Freitag" -> 5
    else -> 0
}

