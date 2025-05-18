package com.ojtapp.mobile.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ojtapp.mobile.components.util.formatAsPeso
import com.ojtapp.mobile.model.GiaRecord
import com.ojtapp.mobile.model.Record
import com.ojtapp.mobile.model.SetupRecord
import kotlinx.coroutines.launch

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
    modifier: Modifier = Modifier,
    onRecordClick: (Pair<String, String>?) -> Unit
) {

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Dimensions.horizontalPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Spacer(Modifier.height(8.dp)) }
        if (records.isEmpty()) {
            item {
                Text(
                    text = "No records found.",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        } else {
            items(
                items = records,
                key = { record ->
                    (record as? GiaRecord)?.id
                        ?: (record as? SetupRecord)?.id
                        ?: record.hashCode()
                }
            ) { record ->
                val scale = remember { Animatable(0.7f) }
                val alpha = remember { Animatable(0f) }

                LaunchedEffect(Unit) {
                    launch {
                        scale.animateTo(
                            targetValue = 1f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                    }
                    launch {
                        alpha.animateTo(
                            targetValue = 1f,
                            animationSpec = tween(durationMillis = 300)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            this.scaleX = scale.value
                            this.scaleY = scale.value
                            this.alpha = alpha.value
                        }
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 16.dp)
                ) {
                    RecordCard(record = record, onRecordClick = onRecordClick)
                }
            }
        }

        item { Spacer(Modifier.height(4.dp)) }
    }
}

@Composable
fun RecordCard(
    modifier: Modifier = Modifier,
    record: Record,
    onRecordClick: (Pair<String, String>?) -> Unit
) {
    Card(
        onClick = {
            when(record){
                is GiaRecord -> onRecordClick(Pair(record.projectTitle, record.fileLocation))
                is SetupRecord -> onRecordClick(Pair(record.firmName,record.fileLocation ?: ""))
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
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
    FieldItem(label = "Cost", value = record.projectCost.formatAsPeso())
    FieldItem(label = "Remarks", value = record.remarks.orEmpty())
    FieldItem(label = "Class", value = record.className)
}

@Composable
private fun SetupRecordContent(record: SetupRecord) {
    RecordHeader(title = record.firmName)
    FieldItem(label = "Proponent", value = record.proponent ?: "")
    FieldItem(label = "District", value = record.district ?: "")
    FieldItem(label = "List of Equipment", value = record.listOfEquipment ?: "")
    FieldItem(label = "Amount Approved", value = record.amountApproved?.formatAsPeso() ?: "")
    FieldItem(label = "Year Approved", value = record.yearApproved.toString())
    FieldItem(label = "Location", value = record.location.orEmpty())
    FieldItem(label = "Sector", value = record.sector)
    FieldItem(label = "Status", value = record.status)
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
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(.35f)
        )
        Text(
            text = value,
            textAlign = TextAlign.Right,
            modifier = Modifier.weight(.65f),
            style = MaterialTheme.typography.bodySmall
        )
    }
}