package com.ojtapp.mobile

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
private fun RecordHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

@Composable
fun RecordCardLayout(
    records: List<Record>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = Dimensions.horizontalPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { Spacer(Modifier.height(4.dp)) }

        if (records.isEmpty()) {
            item {
                Text(
                    text = "No records found. 😭",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 32.dp)
                )
            }
        } else {
            items(records) { record ->
                RecordCard(record = record)
                Spacer(Modifier.height(12.dp))
            }
        }

        item { Spacer(Modifier.height(4.dp)) }
    }
}

@Composable
fun RecordCard(
    record: Record,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            when (record) {
                is GiaRecord -> GiaRecordContent(record)
                is SetupRecord -> SetupRecordContent(record)
                else -> Text("Unknown Record Type 😵‍💫")
            }
        }
    }
}

@Composable
private fun GiaRecordContent(record: GiaRecord) {
    RecordHeader(title = record.projectTitle)
    FieldItem(label = "Beneficiary", value = record.beneficiary)
    FieldItem(label = "Location", value = record.location)
    FieldItem(label = "Duration", value = record.projectDuration)
    FieldItem(label = "Cost", value = record.projectCost.toString())
    record.remarks?.let { FieldItem(label = "Remarks", value = it) }
    FieldItem(label = "Class", value = record.className)
}

@Composable
private fun SetupRecordContent(record: SetupRecord) {
    FieldItem(label = "Id", value = record.id.toString())
    FieldItem(label = "Firm Name", value = record.firmName)
    FieldItem(label = "Proponent", value = record.components ?: "")
    FieldItem(label = "District", value = record.district ?: "")
    FieldItem(label = "List of Equipment", value = record.listOfEquipment ?: "")
    FieldItem(label = "Amount Approved", value = record.amountApproved.toString())
    FieldItem(label = "Year Approved", value = record.yearApproved.toString())
    FieldItem(label = "File Location", value = record.fileLocation)
}

@Composable
private fun FieldItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall
        )
    }
}