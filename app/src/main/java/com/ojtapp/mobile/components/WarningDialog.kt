package com.ojtapp.mobile.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.ojtapp.mobile.R

@Composable
fun WarningDialog(modifier: Modifier = Modifier, onDismissRequest: () -> Unit, onLogout: () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card {
            Text(
                text = stringResource(R.string.logout_warning)
            )
            Row(
                modifier = Modifier.align(Alignment.End)
            ) {
                TextButton(onClick = {
                    onDismissRequest()
                    onLogout()
                }) { Text("confirm") }
                TextButton(onClick = onDismissRequest) { Text("cancel") }
            }
        }
    }
}