package com.ojtapp.mobile

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<String>
}