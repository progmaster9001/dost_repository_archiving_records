package com.ojtapp.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ojtapp.mobile.ui.theme.DOSTRepositoryArchivingRecordsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DOSTRepositoryArchivingRecordsTheme {
                RarNavigationHost()
            }
        }
    }
}