package de.pause.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val email: String, val password: String)

typealias RegisterRequest = LoginRequest

@Serializable
data class MenuInfo(
    val menuId: Int,
    val validFrom: String,
    val validTo: String,
    val dish: String,
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
    val role: String,
)