package com.ojtapp.mobile.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Serializable
object MainGraph

@Composable
fun RarNavigationHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    Scaffold(
        contentWindowInsets = WindowInsets(0.dp)
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Login,
            modifier = Modifier.padding(innerPadding)
        ){
            loginScreen(onLogin = navController::navigateToHomeScreen)
            navigation<MainGraph>(
                startDestination = Home
            ){
                homeScreen(onLogout = navController::navigateToLogin, onFileClick = navController::navigateToFilePickerGraph)
                filePickerGraph(fileClick = navController::navigateToFilePickerScreen, back = navController::popBackStack)
            }
        }
    }
}