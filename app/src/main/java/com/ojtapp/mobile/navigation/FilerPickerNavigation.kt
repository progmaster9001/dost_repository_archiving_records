package com.ojtapp.mobile.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.ui.unit.center
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.ojtapp.mobile.screens.FilePickerRoute
import kotlinx.serialization.Serializable

@Serializable
object FilePickerGraph

@Serializable
data class FilerPicker(val path: String)

fun NavController.navigateToFilePickerGraph(){
    navigate(FilePickerGraph)
}

fun NavController.navigateToFilePickerScreen(path: String){
    navigate(FilerPicker(path))
}

fun NavGraphBuilder.filePickerGraph(fileClick: (String) -> Unit, back: () -> Unit){
    navigation<FilePickerGraph>(
        startDestination = FilerPicker(path = ""),
        enterTransition = { slideInVertically(initialOffsetY = { 120 }) },
        exitTransition = { slideOutVertically(targetOffsetY = { 320 }) + fadeOut(tween(durationMillis = 250)) }
    ){
        composable<FilerPicker> {
            FilePickerRoute(
                currentPath = it.toRoute<FilerPicker>().path,
                fileClick = fileClick,
                back = back,
            )
        }
    }
}