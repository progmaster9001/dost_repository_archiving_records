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
import androidx.compose.ui.text.style.TextAlign

@Composable
fun RecordCardLayout(
    records: List<Record>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Dimensions.basicSpacing)
    ) {
        item { Spacer(modifier = Modifier.height(Dimensions.verticalPadding) )}
        if(records.isEmpty()){
            item { Text(modifier = Modifier.fillMaxWidth(), text = "No records found", textAlign = TextAlign.Center) }
        }else{
            items(records){ record ->
                RecordCard(record)
            }
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