package de.pause.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userId: String,
    val email: String,
    val password: String,
    val role: UserRole
)