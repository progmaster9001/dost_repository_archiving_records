package com.ojtapp.mobile

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object Login

fun NavController.navigateToLogin(){
    navigate(Login){
        popUpToRoute
    }
}

fun NavGraphBuilder.loginScreen(onLogin: () -> Unit){
    composable<Login> {
        val loginViewModel: LoginViewModel = viewModel(factory = GenericViewModelFactory {
            LoginViewModel(LocalAuthRepository(), ServiceLocator.getUserRepository())
        })
        LoginRoute(viewModel = loginViewModel,onLogin = onLogin)
    }
}
