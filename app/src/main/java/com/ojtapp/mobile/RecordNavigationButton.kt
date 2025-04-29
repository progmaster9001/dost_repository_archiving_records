package com.ojtapp.mobile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

sealed interface RecordNavigationEvent{
    data object Next: RecordNavigationEvent
    data object Back: RecordNavigationEvent
    data class Jump(val page: Int): RecordNavigationEvent
}

@Composable
fun RecordNavigationButton(
    modifier: Modifier = Modifier,
    currentPage: Int,
    totalPages: Int,
    recordNavigationEvent: (RecordNavigationEvent) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = RoundedCornerShape(8.dp), // smaller corner radius
        shadowElevation = 2.dp, // smaller shadow
        modifier = modifier
            .padding(4.dp) // smaller outer padding
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 6.dp, vertical = 3.dp) // tighter inside padding
                .wrapContentWidth()
                .height(36.dp), // slightly shorter
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp) // tighter spacing
        ) {
            // Previous Button
            IconButton(
                onClick = { recordNavigationEvent(RecordNavigationEvent.Back) },
                enabled = currentPage > 1,
                modifier = Modifier.size(28.dp) // smaller icon button size
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Previous",
                    tint = if (currentPage > 1) Color.Gray else Color(0xFFCCCCCC)
                )
            }

            // First page
            PageButton(
                page = 1,
                isSelected = currentPage == 1,
                textStyle = MaterialTheme.typography.bodySmall, // smaller font
                size = 30.dp // smaller button
            ) {
                recordNavigationEvent(RecordNavigationEvent.Jump(0))
            }

            if (currentPage > 3) {
                Text(
                    "...",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 2.dp)
                )
            }

            // Middle pages
            val startPage = maxOf(2, currentPage - 1)
            val endPage = minOf(totalPages - 1, currentPage + 1)

            (startPage..endPage).forEach { page ->
                PageButton(
                    page = page,
                    isSelected = currentPage == page,
                    textStyle = MaterialTheme.typography.bodySmall,
                    size = 30.dp
                ) {
                    recordNavigationEvent(RecordNavigationEvent.Jump(page))
                }
            }

            if (currentPage < totalPages - 2) {
                Text(
                    "...",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 2.dp)
                )
            }

            // Last page
            if (totalPages > 1) {
                PageButton(
                    page = totalPages,
                    isSelected = currentPage == totalPages,
                    textStyle = MaterialTheme.typography.bodySmall,
                    size = 30.dp
                ) {
                    recordNavigationEvent(RecordNavigationEvent.Jump(totalPages))
                }
            }

            // Next Button
            IconButton(
                onClick = { recordNavigationEvent(RecordNavigationEvent.Next) },
                enabled = currentPage < totalPages,
                modifier = Modifier.size(28.dp) // smaller icon button size
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Next",
                    tint = if (currentPage < totalPages) Color.Gray else Color(0xFFCCCCCC)
                )
            }
        }
    }
}

@Composable
private fun PageButton(
    page: Int,
    isSelected: Boolean,
    textStyle: TextStyle,
    size: Dp,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF007bff) else Color.Transparent,
            contentColor = if (isSelected) Color.White else Color.Gray
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        border = if (isSelected) null else BorderStroke(0.dp, Color.Transparent),
        shape = RoundedCornerShape(6.dp),
        contentPadding = PaddingValues(0.dp), // no extra padding
        modifier = Modifier.size(size)
    ) {
        Text(
            text = page.toString(),
            style = textStyle,
        )
    }
}