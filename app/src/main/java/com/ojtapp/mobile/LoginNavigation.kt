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
        popUpTo<Home>{
            inclusive = true
        }
    }
}

fun NavGraphBuilder.loginScreen(onLogin: () -> Unit){
    composable<Login> {
        val loginViewModel: LoginViewModel = viewModel(factory = GenericViewModelFactory {
            LoginViewModel(ServiceLocator.getAuthRepository(), ServiceLocator.getUserRepository())
        })
        LoginRoute(viewModel = loginViewModel, onLogin = onLogin)
    }
}
