package com.ojtapp.mobile

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RarTextField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        label = {
            Text(label)
        },
        value = value,
        onValueChange = {
            onValueChange(it)
        }
    )
}