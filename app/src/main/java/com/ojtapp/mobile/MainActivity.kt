package com.ojtapp.mobile

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.RippleConfiguration
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import com.ojtapp.mobile.navigation.RarNavigationHost
import com.ojtapp.mobile.ui.theme.DOSTRepositoryArchivingRecordsTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.auto(lightScrim = Color.TRANSPARENT, darkScrim = Color.TRANSPARENT)
        )
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            DOSTRepositoryArchivingRecordsTheme {
                RarNavigationHost()
            }
        }
    }
}