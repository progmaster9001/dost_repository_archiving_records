package com.ojtapp.mobile.repositories.remote

import android.content.SharedPreferences
import com.ojtapp.mobile.repositories.AuthRepository
import com.ojtapp.mobile.model.RarApiService
import retrofit2.HttpException

class RemoteAuthRepository(
    private val apiService: RarApiService,
    private val sharedPref: SharedPreferences
): AuthRepository {
    override suspend fun login(email: String, password: String): Result<String> {
        return try {
            val response = apiService.login(mapOf("email" to email, "password" to password))
            if (response.isSuccessful && response.body()?.token != null) {
                val token = response.body()!!.token
                sharedPref.edit().putString("jwt_token", token).apply()
                Result.success(token)
            } else {
                Result.failure(Exception("Login failed: ${response.message()}"))
            }
        } catch (e: HttpException) {
            Result.failure(Exception("Network error: ${e.message()}"))
        } catch (e: Exception) {
            Result.failure(Exception("Unexpected error: ${e.localizedMessage}"))
        }
    }
}