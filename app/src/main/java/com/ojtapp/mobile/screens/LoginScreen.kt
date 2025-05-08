package com.ojtapp.mobile.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ojtapp.mobile.R
import com.ojtapp.mobile.components.Dimensions
import com.ojtapp.mobile.components.RarButton
import com.ojtapp.mobile.components.RarTextField
import com.ojtapp.mobile.data.RepositoryMode
import com.ojtapp.mobile.viewmodels.LoginState
import com.ojtapp.mobile.viewmodels.LoginViewModel
import org.w3c.dom.Text

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
    val currentMode by viewModel.switchMode.collectAsStateWithLifecycle()
    val switchMessage by viewModel.switchStatus.collectAsStateWithLifecycle()

    LaunchedEffect(hasToken) {
        Log.d("HasToken", "Has token: $hasToken")
        if(hasToken) onLogin()
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
        currentMode = currentMode,
        switchMessage = switchMessage,
        click = { isDialogTriggered = true },
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        switchRepository = viewModel::switchRepositoryMode,
        login = viewModel::login
    )
}

@Composable
private fun LoginScreen(
    state: LoginState,
    message: String?,
    currentMode: RepositoryMode,
    switchMessage: String?,
    click: () -> Unit,
    modifier: Modifier = Modifier,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    switchRepository: (RepositoryMode) -> Unit,
    login: (String, String) -> Unit
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(top = 16.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
    ) {
        Image(
            painter = painterResource(R.drawable.dost_seal),
            contentDescription = "login_logo",
            modifier = Modifier.scale(1.2f)
        )
        Text(
            text = "Repository Archiving Records",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 24.dp, horizontal = 32.dp)
        )
        Column{
            Text(
                text = "Sign in",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 32.dp, start = 32.dp, end = 32.dp)
            )
            Column(
                modifier = Modifier.padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                RarTextField(
                    label = "Email",
                    placeHolder = "jd.cruz@gmail.com",
                    leadingIcon = R.drawable.envelope,
                    value = state.email,
                    onValueChange = onEmailChange,
                    togglePassword = {}
                )
                RarTextField(
                    label = "Password",
                    value = state.password,
                    leadingIcon = R.drawable.lock,
                    trailingIcon = if (isPasswordVisible) R.drawable.crossed_eye else R.drawable.eye_open_,
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    onValueChange = onPasswordChange,
                    togglePassword = { isPasswordVisible = !isPasswordVisible }
                )
                Spacer(Modifier.height(16.dp))
                OutlinedButton(
                    onClick = {
                        switchRepository(
                            if (currentMode == RepositoryMode.REMOTE) RepositoryMode.LOCAL else RepositoryMode.REMOTE
                        )
                    },
                    modifier = Modifier.width(300.dp),
                    shape = RoundedCornerShape(25f)
                ) {
                    switchMessage?.let {
                        Text(
                            it,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
                RarButton(
                    enabled = switchMessage != "Switching repositories..."
                ) {
                    login(state.email, state.password)
                }
            }
            Spacer(Modifier.height(54.dp))
        }
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