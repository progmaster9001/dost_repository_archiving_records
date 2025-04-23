package com.ojtapp.mobile

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun LayoutSwitch(
    modifier: Modifier = Modifier,
    isTableLayout: Boolean,
    setLayout: () -> Unit,
    iconSize: Dp = 24.dp // Default size
) {
    val activeColor = Color(0xFF9C27B0)
    val inactiveColor = Color.White

    val buttonSize = iconSize * 1.5f
    val spacing = 8.dp

    val offsetX: Dp by animateDpAsState(
        targetValue = if (isTableLayout) 0.dp else buttonSize + spacing,
        label = "SlideOffset"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(activeColor)
            .padding(4.dp)
            .height(buttonSize)
            .width(buttonSize * 2 + spacing)
            .clickable { setLayout() } // clickable sa tanan!
    ) {
        // BACKGROUND Color (stay at the back)
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(50))
                .background(activeColor)
                .zIndex(0f)
        )

        // SLIDING background -- SMALLER and CENTERED
        Box(
            modifier = Modifier
                .offset(x = offsetX)
                .size(buttonSize) // instead of fillMaxHeight() + width()
                .clip(RoundedCornerShape(50))
                .background(inactiveColor)
                .align(Alignment.CenterStart) // ALIGN VERTICALLY CENTERED 🔥
                .zIndex(1f)
        )

        // ICONS
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = spacing / 2)
                .zIndex(2f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LayoutSwitchItem(
                icon = R.drawable.table_list__1_,
                isSelected = isTableLayout,
                activeColor = activeColor,
                inactiveColor = inactiveColor,
                iconSize = iconSize
            )
            LayoutSwitchItem(
                icon = R.drawable.cards_blank__1_,
                isSelected = !isTableLayout,
                activeColor = activeColor,
                inactiveColor = inactiveColor,
                iconSize = iconSize
            )
        }
    }
}

@Composable
private fun LayoutSwitchItem(
    icon: Int,
    isSelected: Boolean,
    activeColor: Color,
    inactiveColor: Color,
    iconSize: Dp
) {
    Box(
        modifier = Modifier
            .size(iconSize * 1.8f),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = if (isSelected) activeColor else inactiveColor,
            modifier = Modifier.size(iconSize)
        )
    }
}