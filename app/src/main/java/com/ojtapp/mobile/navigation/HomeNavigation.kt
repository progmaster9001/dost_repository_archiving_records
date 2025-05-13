package com.ojtapp.mobile.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ojtapp.mobile.viewmodels.GenericViewModelFactory
import com.ojtapp.mobile.data.ServiceLocator
import com.ojtapp.mobile.screens.MainRoute
import com.ojtapp.mobile.viewmodels.MainViewModel
import kotlinx.serialization.Serializable


@Serializable
object Home

fun NavController.navigateToHomeScreen(){
    navigate(MainGraph){
        popUpTo<Login>{
            inclusive = true
        }
    }
}

fun NavGraphBuilder.homeScreen(
    onLogout: () -> Unit,
    onRecordClick: (String) -> Unit,
    onFileClick: () -> Unit
){
    composable<Home> {
        val viewModel = viewModel<MainViewModel>()
        MainRoute(
            viewModel = viewModel,
            logout = onLogout,
            onRecordClick = onRecordClick,
            onFileClick = onFileClick
        )
    }
}