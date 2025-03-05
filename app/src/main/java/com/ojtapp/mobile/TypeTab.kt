package com.ojtapp.mobile

import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TypeTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    onSelectedTab: (Type) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
    ) {
        Type.entries.forEachIndexed { index, type ->
            TypeTab(
                type = type,
                selected = selectedTabIndex == index,
                onSelectedTab = {
                    onSelectedTab(type)
                }
            )
        }
    }
}

@Composable
fun TypeTab(
    type: Type,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onSelectedTab: () -> Unit
) {
    Tab(
        selected = selected,
        text = {
            Text(type.name)
        },
        onClick = onSelectedTab
    )
}