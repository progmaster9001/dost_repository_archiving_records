package com.ojtapp.mobile

import android.content.Context
import android.content.SharedPreferences

object ServiceLocator {
    private lateinit var apiClient: RarApiClient
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var recordsRepository: RecordsRepository
    private lateinit var userRepository: UserRepository
    private lateinit var userPreference: UserPreference
    private lateinit var apiService: RarApiService

    fun init(context: Context){
        apiClient = RarApiClient()
        userRepository = TestUserRepository()
        sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        userPreference = UserPreference(sharedPreferences)
        apiService = apiClient.build
    }

    fun provideRecordsRepository(recordsRepository: RecordsRepository): RecordsRepository{
        this.recordsRepository = recordsRepository
        return recordsRepository
    }

    fun getRecordsRepository() = recordsRepository
    fun getUserRepository() = userRepository
    fun getApiService() = apiService
    fun getUserPreference() = userPreference
}