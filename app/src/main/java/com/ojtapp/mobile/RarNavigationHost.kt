package com.ojtapp.mobile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController


@Composable
fun RarNavigationHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Login
    ){
        loginScreen(onLogin = navController::navigateToHomeScreen)
        homeScreen(onLogout = navController::navigateToLogin)
    }
}