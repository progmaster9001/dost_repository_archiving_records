package com.ojtapp.mobile.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun RarButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.width(300.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(108, 172, 192, 255)
        ),
        shape = RoundedCornerShape(25f),
        onClick = onClick
    ) {
        Text("Sign in", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
    }
}