package com.ojtapp.mobile.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ojtapp.mobile.R
import com.ojtapp.mobile.model.Type

@Composable
fun TypeTabRow(
    filterAmount: Int,
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    onFilterSelect: () -> Unit,
    onSelectedTab: (Type) -> Unit,
    onFileClick: () -> Unit
) {
    Surface(
        modifier = Modifier,
        shape = RoundedCornerShape(70f),
        color = MaterialTheme.colorScheme.inverseSurface,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(70f))
                .background(Color.Transparent),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Type.entries.forEachIndexed { index, type ->
                TypeTab(
                    type = type,
                    modifier = Modifier
                        .then(
                            if (index == 0) Modifier.clip(
                                RoundedCornerShape(
                                    topStart = 70f,
                                    bottomStart = 70f
                                )
                            ) else Modifier
                        ),
                    selected = selectedTabIndex == index,
                    onSelectedTab = {
                        onSelectedTab(type)
                    }
                )
            }

            Box(
                modifier = modifier
                    .height(38.dp)
                    .width(50.dp)
                    .clickable(onClick = onFileClick),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.folder),
                    contentDescription = "file_folder",
                    modifier = Modifier.size(24.dp)
                )
            }

            Box(
                modifier = Modifier
                    .height(38.dp)
                    .width(50.dp)
                    .clip(RoundedCornerShape(topEnd = 70f, bottomEnd = 70f))
                    .clickable { onFilterSelect() },
                contentAlignment = Alignment.Center
            ) {
                BadgedBox(
                    badge = {
                        if(filterAmount > 0) Badge(content = { Text("$filterAmount") })
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "filter_menu",
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
    val animatedScale by animateFloatAsState(targetValue = targetScale)

    Box(
        modifier = modifier
            .sizeIn(
                minHeight = 38.dp,
                minWidth = 90.dp
            )
            .background(if (selected) Color(133, 224, 224, 255) else MaterialTheme.colorScheme.inverseSurface)
            .clickable(onClick = onSelectedTab)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = type.name,
            modifier = Modifier.scale(animatedScale),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelLarge,
            color = if (selected) Color(23, 95, 108, 255) else LocalContentColor.current
        )
    }
}