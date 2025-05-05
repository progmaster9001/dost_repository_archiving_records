package com.ojtapp.mobile

import android.content.Context
import android.content.SharedPreferences

object ServiceLocator {
    private lateinit var apiClient: RarApiClient
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var recordsRepository: RecordsRepository
    private lateinit var userRepository: UserRepository
    private lateinit var userPreference: UserPreference
    private lateinit var authRepository: AuthRepository
    private lateinit var fileRepository: FileRepository
    private lateinit var apiService: RarApiService

    fun init(context: Context){
        apiClient = RarApiClient()
        sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        userPreference = UserPreference(sharedPreferences)
        userRepository = UserRepositoryImpl(userPreference = userPreference)
        apiService = apiClient.build
        authRepository = RemoteAuthRepository(apiService = apiService, sharedPref = sharedPreferences)
        fileRepository = FileRepositoryImpl(api = apiService)
        recordsRepository = RemoteRecordsRepository(rarApiService = apiService, sharedPref = sharedPreferences)
    }

    fun getRecordsRepository() = recordsRepository
    fun getUserRepository() = userRepository
    fun getAuthRepository() = authRepository
    fun getFileRepository() = fileRepository
    fun getApiService() = apiService
    fun getUserPreference() = userPreference
}