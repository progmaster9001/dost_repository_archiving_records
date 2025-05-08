package com.ojtapp.mobile.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ojtapp.mobile.components.Dimensions
import com.ojtapp.mobile.components.RarButton
import com.ojtapp.mobile.components.RarTextField
import com.ojtapp.mobile.data.RepositoryMode
import com.ojtapp.mobile.viewmodels.LoginState
import com.ojtapp.mobile.viewmodels.LoginViewModel

@Composable
fun LoginRoute(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel,
    onLogin: () -> Unit
) {

    val state by viewModel.loginState.collectAsStateWithLifecycle()
    val message by viewModel.errorMessage.collectAsStateWithLifecycle()
    val hasToken by viewModel.hasToken.collectAsStateWithLifecycle()
    var isDialogTriggered by remember { mutableStateOf(false) }
    val switchMessage by viewModel.switchStatus.collectAsStateWithLifecycle()

    LaunchedEffect(hasToken) {
        Log.d("HasToken", "Has token: $hasToken")
        if(hasToken) onLogin()
    }

    LaunchedEffect(message) {
        if(message?.isNotEmpty() == true) isDialogTriggered = true
    }

    SwitchDialog(
        isDialogTriggered = isDialogTriggered,
        switchMessage = switchMessage,
        onDismissRequest = { isDialogTriggered = false },
        switchRepository = viewModel::switchRepositoryMode
    )

    LoginScreen(
        state = state,
        message = message,
        click = { isDialogTriggered = true },
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        login = viewModel::login
    )
}

@Composable
private fun LoginScreen(
    state: LoginState,
    message: String?,
    click: () -> Unit,
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
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = onPasswordChange
        )
        Button(onClick = click) { Text("nack") }
        RarButton {
            login(state.email, state.password)
        }
        if(message != null) Text(message)
    }
}

@Composable
fun SwitchDialog(
    modifier: Modifier = Modifier,
    switchMessage: String?,
    isDialogTriggered: Boolean,
    onDismissRequest: () -> Unit,
    switchRepository: (RepositoryMode) -> Unit
) {
    if(isDialogTriggered){
        Dialog(onDismissRequest = onDismissRequest) {
            Card {
                Text("Do you want to switch to Repositories?")
                Row {
                    Button(onClick = { switchRepository(RepositoryMode.LOCAL)}) { Text("Local") }
                    Button(onClick = { switchRepository(RepositoryMode.REMOTE)}) { Text("Remote") }
                    Button(onClick = onDismissRequest) { Text("No") }
                }
                if(switchMessage != null) Text(switchMessage)
            }
        }
    }
}