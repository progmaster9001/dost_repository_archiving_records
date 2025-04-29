package com.ojtapp.mobile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RecordTableLayout(
    records: List<Record>,
    modifier: Modifier = Modifier,
    onRecordClick: (Record) -> Unit = {}
) {
    if (records.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))
            Text("No records found.")
        }
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(8.dp),
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // Header Section
                RecordHeader(records)
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                )
            }
        }

        itemsIndexed(records) { index, record ->
            Card(
                shape = RoundedCornerShape(0.dp), // No rounding for list items
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .clickable { onRecordClick(record) }
                ) {
                    RarRecord(record, onClick = { onRecordClick(record) })

                    if (index != records.lastIndex) {
                        HorizontalDivider(
                            thickness = 0.5.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RecordHeader(
    records: List<Record>,
    modifier: Modifier = Modifier
) {
    val fieldNames = records.firstOrNull()?.let { getFieldNames(it) } ?: emptyList()

    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        fieldNames.forEachIndexed { index, column ->
            if (column != null) {
                RarCell(
                    value = column,
                    index = index,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    isHeader = true
                )
            }
        }
    }
}

@Composable
fun RarRecord(
    record: Record,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val fieldValues = getFieldValues(record) ?: emptyList()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        fieldValues.forEachIndexed { index, value ->
            if (value != null) {
                RarCell(
                    value = value,
                    index = index,
                    fontWeight = if (index == 0) FontWeight.SemiBold else null,
                    specialStyling = if(LocalRecordTab.current == Type.GIA) index == 6 else index == 5
                )
            }
        }
    }
}

@Composable
fun RarCell(
    value: String,
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current,
    fontWeight: FontWeight? = null,
    isHeader: Boolean = false,
    specialStyling: Boolean = false,
    index: Int,
) {
    val width = when (index) {
        0 -> 60.dp  // ID, so way smaller
        1 -> 150.dp // Status pill gets decent breathing room
        else -> 120.dp // Rate, Balance, Deposit flex wider
    }

    Box(
        modifier = modifier
            .heightIn(min = 40.dp)
            .width(width),
        contentAlignment = Alignment.CenterStart
    ) {
        if (specialStyling) {
            StatusPill(value = value)
        } else {
            Text(
                text = value,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = color,
                fontWeight = fontWeight ?: FontWeight.Normal,
                style = if (isHeader) MaterialTheme.typography.labelMedium else MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
fun StatusPill(value: String) {
    val backgroundColor = when (value.lowercase()) {
        "open" -> Color(0xFFD1FAE5) // green-ish
        "paid" -> Color(0xFFDBEAFE) // blue-ish
        "inactive" -> Color(0xFFE0E0E0) // gray
        else -> Color(0xFFE0E7FF) // fallback soft blue
    }
    val textColor = when (value.lowercase()) {
        "open" -> Color(0xFF065F46)
        "paid" -> Color(0xFF1D4ED8)
        "inactive" -> Color(0xFF6B7280)
        else -> Color(0xFF4338CA)
    }

    Box(
        modifier = Modifier
            .background(backgroundColor, shape = RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}

fun getFieldNames(record: Record): List<String?>? = when (record) {
    is GiaRecord -> giaFieldNames
    is SetupRecord -> setupFieldNames
    else -> null
}

fun getFieldValues(record: Record): List<String?>? = getFieldNames(record)?.map { it?.let { name -> getFieldValue(record, name) } }