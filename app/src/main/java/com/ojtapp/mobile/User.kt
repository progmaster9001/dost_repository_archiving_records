package com.ojtapp.mobile

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val name: String = "",
    val email: String = "",
    val token: String = ""
)