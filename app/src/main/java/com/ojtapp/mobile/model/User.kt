package com.ojtapp.mobile.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val name: String = "",
    val email: String = "",
    val token: String = ""
)