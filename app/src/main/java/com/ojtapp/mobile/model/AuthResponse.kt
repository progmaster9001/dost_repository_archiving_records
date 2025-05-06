package com.ojtapp.mobile.model

data class AuthResponse(
    val token: String,
    val user: AuthUser
)

data class AuthUser(
    val id: Int,
    val username: String,
    val email: String? = null,
    val role: String? = null
)