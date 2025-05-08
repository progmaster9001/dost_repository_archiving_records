package com.ojtapp.mobile.repositories

import com.ojtapp.mobile.data.RepositoryMode
import com.ojtapp.mobile.data.ServiceLocator
import com.ojtapp.mobile.repositories.local.LocalAuthRepository
import com.ojtapp.mobile.repositories.local.LocalFileRepository
import com.ojtapp.mobile.repositories.local.LocalRecordsRepository
import com.ojtapp.mobile.repositories.local.TestUserRepository
import com.ojtapp.mobile.repositories.remote.FileRepositoryImpl
import com.ojtapp.mobile.repositories.remote.RemoteAuthRepository
import com.ojtapp.mobile.repositories.remote.RemoteRecordsRepository
import com.ojtapp.mobile.repositories.remote.UserRepositoryImpl

interface RepositoryProvider {
    val mode: RepositoryMode
    val recordsRepository: RecordsRepository
    val userRepository: UserRepository
    val authRepository: AuthRepository
    val fileRepository: FileRepository
}

object RemoteOnlyProvider : RepositoryProvider {
    override val mode = RepositoryMode.REMOTE
    override val recordsRepository by lazy {
        RemoteRecordsRepository(ServiceLocator.getApiService(), ServiceLocator.getSharedPreference())
    }
    override val userRepository by lazy {
        UserRepositoryImpl(ServiceLocator.getUserPreference())
    }
    override val authRepository by lazy {
        RemoteAuthRepository(ServiceLocator.getApiService(), ServiceLocator.getSharedPreference())
    }
    override val fileRepository by lazy {
        FileRepositoryImpl(ServiceLocator.getApiService())
    }
}

object LocalOnlyProvider : RepositoryProvider {
    override val mode = RepositoryMode.LOCAL
    override val recordsRepository by lazy {
        LocalRecordsRepository()
    }
    override val userRepository by lazy {
        TestUserRepository(ServiceLocator.getUserPreference())
    }
    override val authRepository by lazy {
        LocalAuthRepository(ServiceLocator.getSharedPreference())
    }
    override val fileRepository by lazy {
        LocalFileRepository()
    }
}