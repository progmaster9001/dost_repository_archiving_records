package com.ojtapp.mobile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun RecordTableLayout(
    records: List<Record>,
    modifier: Modifier = Modifier,
    onRecordClick: () -> Unit = { }
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (records.isEmpty()) item { Text("No records found.") }
        item {  RecordHeader(records) }
        itemsIndexed(records, key = { index, _ ->  index }){ _, record ->
            RarRecord(record, onClick = onRecordClick)
        }
    }
}

@Composable
fun RecordHeader(
    records: List<Record>,
    modifier: Modifier = Modifier
) {
    val fieldNames = records.firstOrNull()?.let { getFieldNames(it) }

    Row(modifier = modifier.height(40.dp)) {
        fieldNames?.forEachIndexed { index, column ->
            if (column != null) {
                RarCell(
                    value = column,
                    index = index,
                    color = MaterialTheme.colorScheme.outline
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
    val fieldValues = getFieldValues(record)

    Row(
        modifier = modifier.height(40.dp).clickable(onClick = onClick)
    ) {
        fieldValues?.forEachIndexed { index, value ->
            if (value != null) {
                RarCell(
                    value = value,
                    index = index,
                    fontWeight = if (index == 1) FontWeight.SemiBold else LocalTextStyle.current.fontWeight
                )
            }
        }
    }
}

@Composable
fun RarCell(
    value: String,
    modifier: Modifier = Modifier,
    color: Color = LocalTextStyle.current.color,
    fontWeight: FontWeight? = LocalTextStyle.current.fontWeight,
    isLastIndex: Boolean = false,
    index: Int,
    size: Int = if(index == 0) 70 else 200,
) {
    Surface(
        modifier = modifier.fillMaxHeight().width(size.dp)
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            if(index == 0) Spacer(modifier = Modifier.width(Dimensions.basicSpacing))
            Text(
                text = value,
                color = color,
                maxLines = 1,
                fontWeight = fontWeight,
            )
            if(isLastIndex) Spacer(modifier = Modifier.height(Dimensions.basicSpacing))
        }
    }
}

fun getFieldNames(record: Record): List<String?>? = when (record) {
    is GiaRecord -> giaFieldNames
    is SetupRecord -> setupFieldNames
    else -> null
}

fun getFieldValues(record: Record): List<String?>? = getFieldNames(record)?.map { it?.let { name -> getFieldValue(record, name) } }