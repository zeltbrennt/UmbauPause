package de.pause.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val email: String, val password: String)

typealias RegisterRequest = LoginRequest

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
data class UserPrincipal(
    val id: String,
    val roles: List<String>,
)

@Serializable
data class OrderCounts(
    val day: Int,
    val orders: Int,
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