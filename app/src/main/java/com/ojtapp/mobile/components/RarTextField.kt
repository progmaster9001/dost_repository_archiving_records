package com.ojtapp.mobile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun RarTextField(
    modifier: Modifier = Modifier,
    leadingIcon: Int,
    trailingIcon: Int? = null,
    label: String,
    value: String,
    placeHolder: String = "",
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onValueChange: (String) -> Unit,
    togglePassword: () -> Unit
) {
    OutlinedTextField(
        label = { Text(label, style = MaterialTheme.typography.titleMedium) },
        value = value,
        leadingIcon = { Icon(painter = painterResource(leadingIcon), contentDescription = "leading_icon", modifier = Modifier.size(24.dp)) },
        trailingIcon = { trailingIcon?.let { Icon(painter = painterResource(it), contentDescription = "trailing_icon", modifier = Modifier.size(24.dp).clickable(onClick = togglePassword)) } },
        textStyle = MaterialTheme.typography.titleMedium,
        placeholder = { Text(placeHolder, style = MaterialTheme.typography.titleMedium) },
        visualTransformation = visualTransformation,
        shape = RoundedCornerShape(25f),
        onValueChange = {
            onValueChange(it)
        },
        modifier = Modifier.width(300.dp)
    )
}