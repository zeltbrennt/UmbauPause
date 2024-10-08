package de.pause.features.shop.data.dto

import kotlinx.serialization.Serializable


@Serializable
data class MenuInfo(
    val validFrom: String,
    val validTo: String,
    val dishes: List<MenuItem>
)

@Serializable
data class MenuItem(
    val id: Int,
    val name: String,
    val day: Int,
)

@Serializable
data class DishDto(
    val id: Int,
    val description: String,
)

@Serializable
data class OrderCounts(
    val day: Int,
    val location: String,
    val orderCount: Int,
)

@Serializable
data class OrderOverview(
    val validFrom: String,
    val validTo: String,
    val timestamp: String,
    val orders: List<OrderCounts>,
)

@Serializable
data class LocationDto(
    val id: Int,
    val name: String,
)

@Serializable
data class OrderDto(
    val item: Int,
    val location: Int,
)