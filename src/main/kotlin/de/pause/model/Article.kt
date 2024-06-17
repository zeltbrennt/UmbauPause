package de.pause.model

import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val name: String,
    val available: Boolean,
    val scheduled: Weekday,
    val price: Double,
)


