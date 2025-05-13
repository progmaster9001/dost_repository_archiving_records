package com.ojtapp.mobile.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ojtapp.mobile.R
import com.ojtapp.mobile.viewmodels.DialogEvent
import com.ojtapp.mobile.viewmodels.Layout

@Composable
fun RarDrawer(
    modifier: Modifier = Modifier,
    currentLayout: Layout,
    setLayout: () -> Unit,
    toggleDialog: (DialogEvent) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
    ) {
        IconButton(
            onClick = { expanded = true }
        ) {
            Icon(imageVector = Icons.Default.Menu, contentDescription = "rar_menu")
        }
        DropdownMenu(
            expanded = expanded,
            shadowElevation = 6.dp,
            onDismissRequest = { expanded = false },
        ) {
            DropdownMenuItem(
                text = {
                    Text("Analytics️")
                },
                leadingIcon = {
                    Icon(painter = painterResource(R.drawable.chart_pie_alt), contentDescription = "analytics_icon", modifier = Modifier.size(24.dp))
                },
                onClick = {
                    expanded = false
                })
            DropdownMenuItem(
                text = {
                    Text("Change History")
                },
                leadingIcon = {
                    Icon(painter = painterResource(R.drawable.time_past), contentDescription = "changes_history_icon", modifier = Modifier.size(24.dp))
                },
                onClick = {
                    expanded = false
                })
            HorizontalDivider()
            DropdownMenuItem(
                text = {
                    Text("Leave", color = MaterialTheme.colorScheme.error)
                },
                leadingIcon = {
                    Icon(painter = painterResource(R.drawable.user_logout__1_), contentDescription = "leave_icon", modifier = Modifier.size(24.dp))
                },
                onClick = {
                    expanded = false
                    toggleDialog(DialogEvent.OpenDialog)
                })
        }
    }
}