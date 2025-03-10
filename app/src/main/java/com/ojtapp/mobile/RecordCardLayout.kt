package com.ojtapp.mobile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun RecordCardLayout(
    records: List<Record>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Dimensions.basicSpacing)
    ) {
        items(records){ record ->
            RecordCard(record)
        }
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