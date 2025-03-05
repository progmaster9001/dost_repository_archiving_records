package com.ojtapp.mobile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginState(
    val email: String = "",
    val password: String = ""
)

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
): ViewModel() {

    val hasToken = userRepository.user
        .map { user -> user.token.isNotEmpty() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    fun onEmailChange(email: String){
        _loginState.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String){
        _loginState.update { it.copy(password = password) }
    }

    fun login(email: String, password: String){
        viewModelScope.launch {
            val result = authRepository.login(email, password)
            result.onSuccess { token ->
                Log.d("token", "Token: $token")
                userRepository.updateUser(User("mj", email, token))
            }
            result.onFailure { e -> _errorMessage.update { e.message } }
        }
    }
}