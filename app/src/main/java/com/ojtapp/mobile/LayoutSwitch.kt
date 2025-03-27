package com.ojtapp.mobile

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LayoutSwitch(isTableLayout: Boolean, setLayout: () -> Unit) {
    val transition = updateTransition(targetState = isTableLayout, label = "Layout Transition")

    // Animate translationX (moves left & right)
    val translationX by transition.animateDp(label = "Translation X Animation") { moved ->
        if (moved) 40.dp else 0.dp // Moves to cover the other icon
    }

    Box(
        modifier = Modifier.padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .background(Color.LightGray)
                .clickable(onClick = setLayout)
                .padding(4.dp), // Padding for better visuals
            contentAlignment = Alignment.CenterStart
        ) {
            // Row containing both icons (side by side)
            Row(
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock Icon", tint = Color.Black)
                Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon", tint = Color.Black)
            }

            // The moving box that slides to cover one icon at a time
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .offset(x = translationX)
                    .background(Color.Blue)
            )
        }
    }

}