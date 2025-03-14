package com.ojtapp.mobile

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
    Box(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())
        ) {
            if(records.isEmpty()){
                Text(modifier = Modifier.align(Alignment.CenterHorizontally), text = "No records found.")
            }else{
                RecordHeader(records)
                records.forEach { record ->
                    Row { RarRecord(record) }
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
    when(records.first()){
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
    index: Int,
    size: Int = if(index == 0) 70 else 200,
) {
    Box(modifier = modifier.width(size.dp)) {
        Text(
            text = value,
            maxLines = 1
        )
    }
}