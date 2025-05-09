package com.ojtapp.mobile.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ojtapp.mobile.R

@Composable
fun RarLoadingProgressIndicator(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(if(isSystemInDarkTheme()) R.raw.loading_anim_dark else R.raw.loading_anim))
    LottieAnimation(
        composition,
        modifier = modifier.size(128.dp),
        iterations = LottieConstants.IterateForever,
    )
}