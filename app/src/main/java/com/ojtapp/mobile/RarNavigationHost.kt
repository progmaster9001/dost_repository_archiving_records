package com.ojtapp.mobile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Serializable
object HomeGraph

@Composable
fun RarNavigationHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Login
    ){
        loginScreen(onLogin = navController::navigateToHomeScreen)
        navigation<HomeGraph>(
            startDestination = Home
        ){
            homeScreen(
                onLogout = navController::navigateToLogin,
                onFileClick = navController::navigateToFilePickerScreen
            )
            filePickerScreen()
        }
    }
}