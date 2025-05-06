package com.ojtapp.mobile.repositories.local

import android.content.SharedPreferences
import com.ojtapp.mobile.repositories.AuthRepository

class LocalAuthRepository(
    private val sharedPref: SharedPreferences
): AuthRepository {
    override suspend fun login(email: String, password: String): Result<String> {
        return try {
            val token = "sample_token"
            sharedPref.edit().putString("jwt_token", token).apply()
            Result.success(token)
        } catch (e: Exception) {
            Result.failure(Exception("Unexpected error: ${e.localizedMessage}"))
        }
    }
}