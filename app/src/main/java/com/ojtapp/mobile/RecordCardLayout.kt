package com.ojtapp.mobile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RecordCardLayout(
    records: List<Record>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth().padding(horizontal = Dimensions.horizontalPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimensions.basicSpacing)
    ) {
        item { Spacer(Modifier.height(8.dp)) }
        if (records.isEmpty()){
            item { Text("No records found.") }
        } else{
            items(records){ record ->
                RecordCard(record)
            }
        }
        item { Spacer(Modifier.height(8.dp)) }
    }
}

@Composable
fun RecordCard(
    record: Record,
    modifier: Modifier = Modifier
) {
    Card{
        Column(
            modifier = modifier.fillMaxWidth().padding(Dimensions.containerPadding)
        ) {
            when(record){
                is GiaRecord -> {
                    giaFieldNames?.forEach { value ->
                        Text(
                            text = getFieldValue(record, value ?: ""),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
                is SetupRecord -> {
                    setupFieldNames?.forEach { value ->
                        Text(
                            text = getFieldValue(record, value ?: ""),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }
}