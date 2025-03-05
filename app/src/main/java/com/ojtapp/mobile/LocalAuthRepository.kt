package com.ojtapp.mobile

class LocalAuthRepository: AuthRepository {
    override suspend fun login(email: String, password: String) = Result.success("sample_token")
}