package com.ojtapp.mobile

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun TypeTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    onSelectedTab: (Type) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        indicator = { }
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

    val targetScale = if (selected) 1.2f else 1f
    val animatedScale by animateFloatAsState(
        targetValue = targetScale,
    )

    Tab(
        selected = selected,
        onClick = onSelectedTab,
        selectedContentColor = Color(133, 224, 224, 255),
        unselectedContentColor = MaterialTheme.colorScheme.outline,
        text = {
            Text(
                text = type.name,
                modifier = Modifier
                    .scale(animatedScale),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelLarge
            )
        },
        modifier = modifier
    )
}