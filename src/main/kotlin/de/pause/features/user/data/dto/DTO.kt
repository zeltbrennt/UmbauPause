package de.pause.features.user.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val email: String, val password: String)

typealias RegisterRequest = LoginRequest


@Serializable
data class UserPrincipal(
    val id: String,
    val roles: List<String>,
)

@Serializable
data class UserDto(
    val email: String,
)