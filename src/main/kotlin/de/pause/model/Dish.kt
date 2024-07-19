package de.pause.model

import kotlinx.serialization.Serializable

@Serializable
data class Dish(
    val description: String,
    val price: Double,
) {
    companion object {
        const val DEFAULT_PRICE = 6.5
    }

    constructor(description: String) : this(description, DEFAULT_PRICE)
}

