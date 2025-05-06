package com.ojtapp.mobile.repositories

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<String>
}