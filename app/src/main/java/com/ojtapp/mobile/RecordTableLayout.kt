package com.ojtapp.mobile

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RecordTableLayout(
    records: List<Record>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.horizontalScroll(rememberScrollState())
    ) {
        RecordHeader(records)
        records.forEach { record ->
            Row { RarRecord(record) }
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
            giaFieldNames?.forEach { column ->
                if (column != null)
                    RarCell(column)
            }
        }
        is SetupRecord -> Row {
            setupFieldNames?.forEach { column ->
                if(column != null)
                    RarCell(column)
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
        is GiaRecord -> giaFieldNames?.forEach { value ->
            if(value != null)
                RarCell(getFieldValue(record, value))
        }
        is SetupRecord -> setupFieldNames?.forEach { value ->
            if(value != null)
                RarCell(getFieldValue(record, value))
        }
    }
}

@Composable
fun RarCell(
    value: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier.width(200.dp)) {
        Text(
            text = value
        )
    }
}