package com.ojtapp.mobile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearDropdownMenu(
    modifier: Modifier = Modifier,
    startYear: Int = 2010,
    endYear: Int = 2025,
    selectedYear: Int?,
    onYearSelected: (Int?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val years = listOf(null) + (startYear..endYear).toList().reversed()
    val selectedText = selectedYear?.toString() ?: "All"

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        Row(modifier = Modifier.width(72.dp).menuAnchor(MenuAnchorType.PrimaryNotEditable), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(selectedText, style = MaterialTheme.typography.labelSmall)
            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
        }
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(72.dp)
        ) {
            years.forEach { year ->
                DropdownMenuItem(
                    text = { Text(year?.toString() ?: "All", style = MaterialTheme.typography.labelSmall) },
                    onClick = {
                        onYearSelected(year)
                        expanded = false
                    }
                )
            }
        }
    }
}