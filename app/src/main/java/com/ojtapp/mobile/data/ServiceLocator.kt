package com.ojtapp.mobile.data

import android.content.Context
import android.content.SharedPreferences
import com.ojtapp.mobile.model.RarApiClient
import com.ojtapp.mobile.model.RarApiService
import com.ojtapp.mobile.model.Resource
import com.ojtapp.mobile.model.UserPreference
import com.ojtapp.mobile.repositories.LocalOnlyProvider
import com.ojtapp.mobile.repositories.RemoteOnlyProvider
import com.ojtapp.mobile.repositories.RepositoryProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update

enum class RepositoryMode {
    REMOTE, LOCAL
}

object ServiceLocator {

    private lateinit var apiClient: RarApiClient
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userPreference: UserPreference
    private lateinit var apiService: RarApiService
    private lateinit var repositoryProvider: MutableStateFlow<RepositoryProvider>

    val currentRepositoryProvider: StateFlow<RepositoryProvider> by lazy {
        checkInitialization()
        repositoryProvider.asStateFlow()
    }

    fun init(context: Context) {
        if (::sharedPreferences.isInitialized) return

        apiClient = RarApiClient()
        sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        userPreference = UserPreference(sharedPreferences)
        apiService = apiClient.build
        repositoryProvider = MutableStateFlow(RemoteOnlyProvider)
    }

    fun switchRepositoryProvider(mode: RepositoryMode): Flow<Resource<String>> = flow {
        checkInitialization()

        if (repositoryProvider.value.mode == mode) {
            emit(Resource.Success("Current Repositories: ${mode.name}"))
            return@flow
        }

        emit(Resource.Loading)
        delay(1500)

        try {
            val provider = when (mode) {
                RepositoryMode.REMOTE -> RemoteOnlyProvider
                RepositoryMode.LOCAL -> LocalOnlyProvider
            }
            repositoryProvider.update { provider }
            emit(Resource.Success("Current Repositories: ${mode.name}"))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error occurred."))
        }
    }

    fun getSharedPreference(): SharedPreferences {
        checkInitialization()
        return sharedPreferences
    }

    fun getApiService(): RarApiService {
        checkInitialization()
        return apiService
    }

    fun getUserPreference(): UserPreference {
        checkInitialization()
        return userPreference
    }

    private fun checkInitialization() {
        if (!::sharedPreferences.isInitialized ||
            !::apiClient.isInitialized ||
            !::userPreference.isInitialized ||
            !::apiService.isInitialized ||
            !::repositoryProvider.isInitialized
        ) {
            throw IllegalStateException("ServiceLocator is not initialized. Call init(context) first!")
        }
    }
}