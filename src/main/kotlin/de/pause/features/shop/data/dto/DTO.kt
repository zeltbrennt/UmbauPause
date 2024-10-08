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
data class SingleOrderDto(
    val item: Int,
    val location: Int,
    val day: Int,
)

@Serializable
data class OrderDto(
    val validFrom: String,
    val validTo: String,
    val orders: List<SingleOrderDto>
)

@Serializable
data class UserOrderDto(
    val date: String,
    val dish: String,
    val location: String,
    val status: String,
)