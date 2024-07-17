package de.pause.model

import kotlinx.serialization.Serializable

@Serializable
data class Menu(
    val Montag: String,
    val Dienstag: String,
    val Mittwoch: String,
    val Donnerstag: String,
    val Freitag: String,
    val validFrom: String,
    val validTo: String,

    )
