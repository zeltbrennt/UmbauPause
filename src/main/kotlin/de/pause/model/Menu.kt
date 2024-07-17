package de.pause.model

import kotlinx.serialization.Serializable

@Serializable
data class Menu(
    val monday: String,
    val tuesday: String,
    val wednesday: String,
    val thursday: String,
    val friday: String,
    val validFrom: String,
    val validTo: String,

    )
