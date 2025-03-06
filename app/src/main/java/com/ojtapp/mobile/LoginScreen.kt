package com.ojtapp.mobile

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LoginRoute(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel,
    onLogin: () -> Unit
) {

    val state by viewModel.loginState.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val hasToken by viewModel.hasToken.collectAsStateWithLifecycle()

    LaunchedEffect(hasToken) {
        Log.d("HasToken", "Has token: $hasToken")
        if(hasToken) onLogin()
    }

    LoginScreen(
        state = state,
        errorMessage = errorMessage,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        login = viewModel::login
    )
}

@Composable
private fun LoginScreen(
    state: LoginState,
    errorMessage: String?,
    modifier: Modifier = Modifier,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    login: (String, String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimensions.verticalPadding, Alignment.CenterVertically)
    ) {
        RarTextField(
            label = "Email",
            value = state.email,
            onValueChange = onEmailChange
        )
        RarTextField(
            label = "Password",
            value = state.password,
            onValueChange = onPasswordChange
        )
        RarButton {
            login(state.email, state.password)
        }
        if(errorMessage != null)
            Text(errorMessage)
    }
}