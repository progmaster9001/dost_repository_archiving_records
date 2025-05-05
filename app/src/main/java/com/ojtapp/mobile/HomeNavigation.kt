package com.ojtapp.mobile

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable


@Serializable
object Home

fun NavController.navigateToHomeScreen(){
    navigate(HomeGraph){
        popUpTo<Login>{
            inclusive = true
        }
    }
}

fun NavGraphBuilder.homeScreen(onLogout: () -> Unit, onFileClick: () -> Unit){
    composable<Home> {
        val viewModel: MainViewModel = viewModel(factory = GenericViewModelFactory {
            MainViewModel(ServiceLocator.getUserRepository(), ServiceLocator.getRecordsRepository())
        })
        MainRoute(
            viewModel = viewModel,
            logout = onLogout,
            onFileClick = onFileClick
        )
    }
}