package com.ojtapp.mobile.data

import android.content.Context
import android.content.SharedPreferences
import com.ojtapp.mobile.model.RarApiClient
import com.ojtapp.mobile.model.RarApiService
import com.ojtapp.mobile.model.Resource
import com.ojtapp.mobile.model.UserPreference
import com.ojtapp.mobile.repositories.AuthRepository
import com.ojtapp.mobile.repositories.FileRepository
import com.ojtapp.mobile.repositories.LocalOnlyProvider
import com.ojtapp.mobile.repositories.UserRepository
import com.ojtapp.mobile.repositories.remote.FileRepositoryImpl
import com.ojtapp.mobile.repositories.RecordsRepository
import com.ojtapp.mobile.repositories.RemoteOnlyProvider
import com.ojtapp.mobile.repositories.RepositoryProvider
import com.ojtapp.mobile.repositories.local.LocalAuthRepository
import com.ojtapp.mobile.repositories.local.LocalFileRepository
import com.ojtapp.mobile.repositories.local.LocalRecordsRepository
import com.ojtapp.mobile.repositories.local.TestUserRepository
import com.ojtapp.mobile.repositories.remote.RemoteAuthRepository
import com.ojtapp.mobile.repositories.remote.RemoteRecordsRepository
import com.ojtapp.mobile.repositories.remote.UserRepositoryImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

enum class RepositoryMode {
    REMOTE, LOCAL
}

object ServiceLocator {

    private lateinit var apiClient: RarApiClient
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userPreference: UserPreference
    private lateinit var repositoryProvider: RepositoryProvider
    private lateinit var apiService: RarApiService
    private lateinit var currentMode: RepositoryMode

    fun init(context: Context){
        apiClient = RarApiClient()
        sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        userPreference = UserPreference(sharedPreferences)
        apiService = apiClient.build
        switchToLocalRepositories()
    }

    private fun switchToRemoteRepositories(){
       repositoryProvider = RemoteOnlyProvider
        currentMode = RepositoryMode.REMOTE
    }

    fun switchToRemoteRepositoriesFlow(mode: RepositoryMode) = flow {
        if(mode != currentMode){
            emit(Resource.Loading)
            delay(1500)
            try{
                switchToRemoteRepositories()
                emit(Resource.Success("ServiceLocator 🛰️ Currently using REMOTE repositories."))
            }catch(e: Exception){
                emit(Resource.Error(e.message ?: "Failed to switch to Remote."))
            }
        }else{
            emit(Resource.Success("ServiceLocator 🛰️ Currently using REMOTE repositories."))
        }
    }

    private fun switchToLocalRepositories(){
        repositoryProvider = LocalOnlyProvider
        currentMode = RepositoryMode.LOCAL
    }

    fun switchToLocalRepositoriesFlow(mode: RepositoryMode) = flow {
        if(mode != currentMode){
            emit(Resource.Loading)
            delay(1500)
            try{
                switchToLocalRepositories()
                emit(Resource.Success("ServiceLocator 📦 Currently using LOCAL repositories."))
            }catch(e: Exception){
                emit(Resource.Error(e.message ?: "Failed to switch to Local."))
            }
        }else{
            emit(Resource.Success("ServiceLocator 📦 Currently using LOCAL repositories."))
        }
    }

    fun getCurrentRepositoryProvider(): String {
        return when (currentMode) {
            RepositoryMode.REMOTE -> "ServiceLocator 🛰️ Currently using REMOTE repositories."
            RepositoryMode.LOCAL -> "ServiceLocator 📦 Currently using LOCAL repositories."
        }
    }

    fun getCurrentRepositoryMode() = currentMode

    fun getSharedPreference() = sharedPreferences
    fun getApiService() = apiService
    fun getUserPreference() = userPreference
    fun getRecordsRepository() = repositoryProvider.recordsRepository
    fun getUserRepository() = repositoryProvider.userRepository
    fun getAuthRepository() = repositoryProvider.authRepository
    fun getFileRepository() = repositoryProvider.fileRepository

}