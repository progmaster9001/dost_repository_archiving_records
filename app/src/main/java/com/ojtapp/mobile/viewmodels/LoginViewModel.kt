package com.ojtapp.mobile.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ojtapp.mobile.data.RepositoryMode
import com.ojtapp.mobile.data.ServiceLocator
import com.ojtapp.mobile.model.Resource
import com.ojtapp.mobile.repositories.AuthRepository
import com.ojtapp.mobile.model.User
import com.ojtapp.mobile.repositories.RepositoryProvider
import com.ojtapp.mobile.repositories.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginState(
    val email: String = "",
    val password: String = ""
)

class LoginViewModel(
    private val serviceLocator: ServiceLocator = ServiceLocator
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _switchMode = MutableStateFlow(ServiceLocator.currentRepositoryProvider.value.mode)

    @OptIn(ExperimentalCoroutinesApi::class)
    val switchStatus = _switchMode.flatMapLatest { mode ->
        serviceLocator.switchRepositoryProvider(mode)
    }.map { resource ->
        when (resource) {
            is Resource.Success -> resource.data
            is Resource.Error -> "Error: ${resource.message}"
            is Resource.Loading -> "Switching repositories..."
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, "Initialized")

    @OptIn(ExperimentalCoroutinesApi::class)
    val hasToken = serviceLocator.currentRepositoryProvider
        .flatMapLatest { provider ->
            provider.userRepository.user
        }
        .map { user -> user.token.isNotEmpty() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = serviceLocator.currentRepositoryProvider.value.authRepository.login(email, password)
            result.onSuccess { token ->
                val user = User("mj", email, token)
                serviceLocator.currentRepositoryProvider.value.userRepository.updateUser(user)
            }
            result.onFailure { e ->
                _errorMessage.value = e.message
            }
        }
    }

    fun onEmailChange(email: String) {
        _loginState.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        _loginState.update { it.copy(password = password) }
    }

    fun switchRepositoryMode(mode: RepositoryMode) {
        _switchMode.update{ mode }
    }
}