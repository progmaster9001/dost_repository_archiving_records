package com.ojtapp.mobile

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.style.TextAlign
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
            .padding(horizontal = Dimensions.horizontalPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { Spacer(Modifier.height(4.dp)) }

        if (records.isEmpty()) {
            item {
                Text(
                    text = "No records found.",
                    style = MaterialTheme.typography.bodyMedium,
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
    FieldItem(label = "Remarks", value = record.remarks.orEmpty())
    FieldItem(label = "Class", value = record.className)
}

@Composable
private fun SetupRecordContent(record: SetupRecord) {
    RecordHeader(title = record.firmName)
    FieldItem(label = "Proponent", value = record.proponent ?: "")
    FieldItem(label = "District", value = record.district ?: "")
    FieldItem(label = "List of Equipment", value = record.listOfEquipment ?: "")
    FieldItem(label = "Amount Approved", value = record.amountApproved.toString())
    FieldItem(label = "Year Approved", value = record.yearApproved.toString())
    FieldItem(label = "Location", value = record.location.orEmpty())
    FieldItem(label = "Sector", value = record.sector)
    FieldItem(label = "Proponent", value = record.status)
}

@Composable
private fun FieldItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = value,
            textAlign = TextAlign.Right,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodySmall
        )
    }
}