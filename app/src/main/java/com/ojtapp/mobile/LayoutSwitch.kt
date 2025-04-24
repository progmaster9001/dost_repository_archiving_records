package com.ojtapp.mobile

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun LayoutSwitch(
    modifier: Modifier = Modifier,
    firstText: String = "Table",
    secondText: String = "Card",
    isTableLayout: Boolean,
    setLayout: () -> Unit
) {
    val scaleFactor = 0.8f

    val totalWidth = (130.dp * scaleFactor)
    val height = (40.dp * scaleFactor)
    val itemWidth = totalWidth / 2
    val cornerRadius = RoundedCornerShape(100f * scaleFactor) // 👀 still thicc just smaller
    val textSize = 14.sp * scaleFactor // or adjust to taste

    val itemWidthPx = with(LocalDensity.current) { itemWidth.toPx() }

    val animatedOffsetX by animateFloatAsState(
        targetValue = if (isTableLayout) 0f else itemWidthPx,
        animationSpec = tween(durationMillis = 300),
        label = "SwitchOffsetAnim"
    )

    Box(
        modifier = modifier
            .width(totalWidth)
            .height(height)
            .clip(cornerRadius)
            .background(MaterialTheme.colorScheme.inverseSurface)
            .clickable(
                indication = null,
                interactionSource = null,
                onClick = setLayout
            )
    ) {
        // Sliding background
        Box(
            modifier = Modifier
                .offset { IntOffset(animatedOffsetX.roundToInt(), 0) }
                .width(itemWidth)
                .fillMaxHeight()
                .clip(cornerRadius)
                .background(MaterialTheme.colorScheme.inverseOnSurface)
        )

        Row(
            modifier = Modifier.fillMaxSize(),
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = firstText,
                    fontSize = textSize,
                    textAlign = TextAlign.Center,
                    color = if (isTableLayout) MaterialTheme.colorScheme.inverseSurface else MaterialTheme.colorScheme.inverseOnSurface,
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = secondText,
                    fontSize = textSize,
                    textAlign = TextAlign.Center,
                    color = if (!isTableLayout) MaterialTheme.colorScheme.inverseSurface else MaterialTheme.colorScheme.inverseOnSurface,
                )
            }
        }
    }
}