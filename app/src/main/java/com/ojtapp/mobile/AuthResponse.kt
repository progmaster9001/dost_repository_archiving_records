package com.ojtapp.mobile

data class AuthResponse(
    val message: String,
    val token: String?,
    val accountType: String?
)