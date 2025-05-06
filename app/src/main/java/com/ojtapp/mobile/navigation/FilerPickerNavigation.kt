package com.ojtapp.mobile.navigation

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
        startDestination = FilerPicker(path = "")
    ){
        composable<FilerPicker> {
            FilePickerRoute(
                path = it.toRoute<FilerPicker>().path,
                fileClick = fileClick,
                back = back,
            )
        }
    }
}