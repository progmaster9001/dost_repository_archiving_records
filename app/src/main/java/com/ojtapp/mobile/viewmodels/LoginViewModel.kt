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

sealed interface SwitchEvent{
    data object Local: SwitchEvent
    data object Remote: SwitchEvent
}

class LoginViewModel: ViewModel() {

    val hasToken = ServiceLocator.getUserRepository().user
        .map { user ->
            user.token.isNotEmpty()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    private val _switchStatus = MutableStateFlow(RepositoryMode.LOCAL)

    @OptIn(ExperimentalCoroutinesApi::class)
    val switchResult = _switchStatus.flatMapLatest { status ->
        when(status){
            RepositoryMode.LOCAL -> ServiceLocator.switchToLocalRepositoriesFlow(status)
            RepositoryMode.REMOTE -> ServiceLocator.switchToRemoteRepositoriesFlow(status)
        }
    }.map { resource ->
        when(resource){
            is Resource.Error -> resource.message
            Resource.Loading -> "Switching..."
            is Resource.Success -> resource.data
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    fun onEmailChange(email: String){
        _loginState.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String){
        _loginState.update { it.copy(password = password) }
    }

    fun login(email: String, password: String){
        viewModelScope.launch {
            val result = ServiceLocator.getAuthRepository().login(email, password)
            result.onSuccess { token ->
                ServiceLocator.getUserRepository().updateUser(User("mj", email, token))
            }
            result.onFailure { e -> _errorMessage.update { e.message } }
        }
    }

    fun switchRepository(event: SwitchEvent) {
        viewModelScope.launch {
            when(event){
                SwitchEvent.Local -> { _switchStatus.update { RepositoryMode.LOCAL } }
                SwitchEvent.Remote -> { _switchStatus.update { RepositoryMode.REMOTE } }
            }
        }
    }
}