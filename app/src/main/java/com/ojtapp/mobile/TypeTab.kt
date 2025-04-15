package com.ojtapp.mobile

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TypeTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    onSelectedTab: (Type) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(40f))
                .background(MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp))
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier
                    .width(LocalConfiguration.current.screenWidthDp.dp * .7f)
                    .padding(2.dp)
                    .clip(RoundedCornerShape(40f)),
                containerColor = Color.Transparent,
                indicator = { },
                divider = { }
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
        modifier = modifier.height(45.dp).padding(2.dp).clip(RoundedCornerShape(40f)).background(TabRowDefaults.primaryContainerColor),
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
        }
    )
}