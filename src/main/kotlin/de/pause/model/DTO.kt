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
    val day: String,
)

@Serializable
data class DishDto(
    val id: Int,
    val description: String,
)

@Serializable
data class UserPrincipal(
    val id: String,
    val role: String,
)