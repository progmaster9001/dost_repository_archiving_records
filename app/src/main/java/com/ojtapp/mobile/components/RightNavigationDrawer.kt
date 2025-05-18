package com.ojtapp.mobile.components

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RightNavigationDrawer(
    modifier: Modifier = Modifier,
    drawerContent: @Composable () -> Unit,
    drawerWidth: Dp = 78.dp,
    isDrawerOpen: Boolean,
    onCloseDrawer: () -> Unit,
    content: @Composable () -> Unit
) {
    val transition = updateTransition(targetState = isDrawerOpen, label = "drawerTransition")

    val contentOffsetX by transition.animateDp(
        label = "contentOffset"
    ) { open ->
        if (open) -drawerWidth else 0.dp
    }

    val drawerOffsetX by transition.animateDp(
        label = "drawerOffset"
    ) { open ->
        if (open) 0.dp else drawerWidth
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(x = contentOffsetX)
        ) {
            content()
        }

        Row(
            modifier = Modifier.fillMaxHeight()
            .width(drawerWidth)
            .align(Alignment.CenterEnd)
            .offset(x = drawerOffsetX)
            .background(MaterialTheme.colorScheme.surfaceBright.copy(.8f))
        ) {
            VerticalDivider()
            drawerContent()
        }
    }
}