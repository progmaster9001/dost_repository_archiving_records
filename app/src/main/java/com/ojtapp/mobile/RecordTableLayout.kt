package com.ojtapp.mobile

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RecordTableLayout(
    records: List<Record>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimensions.basicSpacing)
    ) {
        if (records.isEmpty()) item { Text("No records found.") }
        item {  RecordHeader(records) }
        itemsIndexed(records, key = { index, _ ->  index}){ _, record ->
            Row{ RarRecord(record) }
        }
    }
}

@Composable
fun RecordHeader(
    records: List<Record>,
    modifier: Modifier = Modifier
) {
    when(records.firstOrNull()){
        is GiaRecord -> Row {
            giaFieldNames?.forEachIndexed { index, column ->
                if (column != null)
                    RarCell(column, index = index)
            }
        }
        is SetupRecord -> Row {
            setupFieldNames?.forEachIndexed { index, column ->
                if(column != null)
                    RarCell(column, index = index)
            }
        }
    }
}

@Composable
fun RarRecord(
    record: Record,
    modifier: Modifier = Modifier
) {
    when (record) {
        is GiaRecord -> giaFieldNames?.forEachIndexed { index, value ->
            if(value != null)
                RarCell(getFieldValue(record, value), index = index)
        }
        is SetupRecord -> setupFieldNames?.forEachIndexed { index, value ->
            if(value != null)
                RarCell(getFieldValue(record, value), index = index)
        }
    }
}

@Composable
fun RarCell(
    value: String,
    modifier: Modifier = Modifier,
    isLastIndex: Boolean = false,
    index: Int,
    size: Int = if(index == 0) 70 else 200,
) {
    Box(modifier = modifier.width(size.dp)) {
        Column {
            Row {
                if(index == 0) Spacer(modifier = Modifier.width(Dimensions.basicSpacing))
                Text(
                    text = value,
                    maxLines = 1
                )
                if(isLastIndex) Spacer(modifier = Modifier.height(Dimensions.basicSpacing))
            }
            Spacer(modifier = Modifier.height(Dimensions.basicSpacing))
            if (!isLastIndex) HorizontalDivider()
        }
    }
}