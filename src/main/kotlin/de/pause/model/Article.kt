package de.pause.model

import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val name: String,
    val available: Boolean,
    val scheduled: String,
    val price: Double,
) {
    val sortOrder: Int
        get() = when (scheduled) {
            "Montag" -> 1
            "Dienstag" -> 2
            "Mittwoch" -> 3
            "Donnerstag" -> 4
            "Freitag" -> 5
            else -> 0
        }
    //TODO: ALS ENUM!!!
}
