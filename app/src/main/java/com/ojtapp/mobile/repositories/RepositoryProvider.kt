package com.ojtapp.mobile.repositories

import com.ojtapp.mobile.data.ServiceLocator
import com.ojtapp.mobile.repositories.local.LocalAuthRepository
import com.ojtapp.mobile.repositories.local.LocalFileRepository
import com.ojtapp.mobile.repositories.local.LocalRecordsRepository
import com.ojtapp.mobile.repositories.local.TestUserRepository
import com.ojtapp.mobile.repositories.remote.FileRepositoryImpl
import com.ojtapp.mobile.repositories.remote.RemoteAuthRepository
import com.ojtapp.mobile.repositories.remote.RemoteRecordsRepository

enum class RepositoryType{
    REMOTE,
    LOCAL
}

interface RepositoryProvider {
    val recordsRepository: RecordsRepository
    val userRepository: UserRepository
    val authRepository: AuthRepository
    val fileRepository: FileRepository
}

object LocalOnlyProvider : RepositoryProvider {
    override val recordsRepository = LocalRecordsRepository()
    override val userRepository = TestUserRepository(ServiceLocator.getUserPreference())
    override val authRepository = LocalAuthRepository(ServiceLocator.getSharedPreference())
    override val fileRepository = LocalFileRepository()
}

object RemoteOnlyProvider : RepositoryProvider {
    override val recordsRepository = RemoteRecordsRepository(ServiceLocator.getApiService(), ServiceLocator.getSharedPreference())
    override val userRepository = TestUserRepository(ServiceLocator.getUserPreference())
    override val authRepository = RemoteAuthRepository(ServiceLocator.getApiService(), ServiceLocator.getSharedPreference())
    override val fileRepository = FileRepositoryImpl(ServiceLocator.getApiService())
}