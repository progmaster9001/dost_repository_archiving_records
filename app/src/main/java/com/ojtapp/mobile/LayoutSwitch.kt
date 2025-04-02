package com.ojtapp.mobile

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LayoutSwitch(isTableLayout: Boolean, setLayout: () -> Unit) {

    val padding = 4.dp
    val boxWidth = 62.dp
    val boxHeight = 32.dp
    val boxShape = RoundedCornerShape(10f)
    val innerBoxSize = boxHeight - (padding * 2)

    val maxOffset = boxWidth - innerBoxSize - (padding * 2)

    val transition = updateTransition(targetState = isTableLayout, label = "Layout Transition")
    val offset by transition.animateDp(label = "Offset Animation") { moved ->
        if (moved) maxOffset else 0.dp
    }

    Box(
        modifier = Modifier.padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(boxWidth, boxHeight)
                .clip(boxShape)
                .background(MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp))
                .clickable(
                    onClick = setLayout
                )
                .padding(padding),
            contentAlignment = Alignment.CenterStart
        ) {

            //            Row(
//                modifier = Modifier.padding(horizontal = 8.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock Icon", tint = Color.Black)
//                Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon", tint = Color.Black)
//            }

            Box(
                modifier = Modifier
                    .size(innerBoxSize)
                    .offset(x = offset)
                    .background(Color.Blue, shape = boxShape)
            )
        }
    }
}