package com.ojtapp.mobile

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object FilerPicker

fun NavController.navigateToFilePickerScreen(){
    navigate(FilerPicker)
}

fun NavGraphBuilder.filePickerScreen(){
    composable<FilerPicker> {
        FilePickerRoute()
    }
}