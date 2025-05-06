package com.ojtapp.mobile.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

    val height = 36.dp
    val totalWidth = 100.dp
    val cornerRadius = RoundedCornerShape(8.dp)
    val textSize = 12.sp

    Box(modifier = modifier){
        Surface(
            modifier = Modifier
                .width(totalWidth)
                .height(height),
            shape = cornerRadius,
            shadowElevation = 2.dp,
            color = Color(0xFFF5F5F5)
        ) {
            Box(
                modifier = Modifier
                    .clip(cornerRadius)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = setLayout
                    )
            ) {
                BoxWithConstraints {
                    val maxWidthPx = with(LocalDensity.current) { maxWidth.toPx() }
                    val halfWidthPx = maxWidthPx / 2f

                    val animatedOffsetX by animateFloatAsState(
                        targetValue = if (isTableLayout) 0f else halfWidthPx,
                        animationSpec = tween(300),
                        label = "SwitchOffset"
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(this@BoxWithConstraints.maxWidth / 2)
                            .offset {
                                IntOffset(animatedOffsetX.roundToInt(), 0)
                            }
                            .clip(cornerRadius)
                            .background(Color(0xFFE0E0E0))
                    )

                    Row(
                        modifier = Modifier.fillMaxSize()
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
                                color = if (isTableLayout) Color(0xFFF5F5F5) else Color.Gray,
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
                                color = if (!isTableLayout) Color(0xFFF5F5F5) else Color.Gray,
                            )
                        }
                    }
                }
            }
        }
    }
}