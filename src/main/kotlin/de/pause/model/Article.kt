package de.pause.model

import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val name: String,
    val type: String,
    val description: String,
    val price: Float,
)
