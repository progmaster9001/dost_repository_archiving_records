package com.ojtapp.mobile.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ojtapp.mobile.viewmodels.GenericViewModelFactory
import com.ojtapp.mobile.data.ServiceLocator
import com.ojtapp.mobile.screens.LoginRoute
import com.ojtapp.mobile.viewmodels.LoginViewModel
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
        val loginViewModel = viewModel<LoginViewModel>()
        LoginRoute(viewModel = loginViewModel, onLogin = onLogin)
    }
}