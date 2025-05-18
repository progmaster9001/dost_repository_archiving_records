package com.ojtapp.mobile.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Surface
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ojtapp.mobile.model.GiaClass
import com.ojtapp.mobile.model.GiaRecord
import com.ojtapp.mobile.model.Record
import com.ojtapp.mobile.model.SectorType
import com.ojtapp.mobile.model.SetupRecord
import com.ojtapp.mobile.model.getFieldValue
import com.ojtapp.mobile.model.giaClassColors
import com.ojtapp.mobile.model.giaFieldNames
import com.ojtapp.mobile.model.sectorTypeColors
import com.ojtapp.mobile.model.setupFieldNames
import com.ojtapp.mobile.screens.RarPieChart
import com.ojtapp.mobile.screens.shadowWithClipIntersect
import kotlinx.coroutines.launch

@Composable
fun RecordTableLayout(
    records: List<Record>,
    modifier: Modifier = Modifier,
    onRecordClick: (Pair<String, String>?) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .horizontalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(records.isEmpty()){
            item {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "No records found.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }else{
            item {
                RecordHeader(records)
            }

            itemsIndexed(records, key = {i, _ -> i}) { index, record ->
                val scale = remember { Animatable(0.9f) }
                val alpha = remember { Animatable(0f) }

                LaunchedEffect(Unit) {
                    launch {
                        scale.animateTo(
                            targetValue = 1f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
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
                Row{
                    Box(modifier = Modifier
                        .height(56.dp)
                        .width(5.dp)
                        .background(
                            when(record){
                                is GiaRecord -> giaClassColors[GiaClass.from(record.className)]
                                is SetupRecord -> sectorTypeColors[SectorType.from(record.sector)]
                                else -> Color.Unspecified
                            } ?: Color.Unspecified
                        )
                    )
                    Box(
                        modifier = Modifier.graphicsLayer {
                            this.scaleX = scale.value
                            this.scaleY = scale.value
                            this.alpha = alpha.value
                        }
                    ){
                        Box(
                            modifier = Modifier.animateItem()
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth().clickable {
                                    when(record){
                                        is GiaRecord -> onRecordClick(Pair(record.projectTitle, record.fileLocation))
                                        is SetupRecord -> onRecordClick(Pair(record.firmName,record.fileLocation ?: ""))
                                    }
                                }
                            ){
                                RarRecord(record)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(2.dp))
            }
        }
    }
}

@Composable
fun RecordHeader(
    records: List<Record>,
    modifier: Modifier = Modifier
) {
    val firstRecord = records.firstOrNull()
    val fieldNames = firstRecord?.let { getFieldNames(it).mapToHeaders(firstRecord) } ?: emptyList()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        fieldNames.forEachIndexed { index, column ->
            RarCell(
                value = column,
                index = index,
                alignment = Alignment.CenterStart,
                fontWeight = FontWeight.Bold,
                isHeader = true,
            )
        }
    }
}

@Composable
fun RarRecord(
    record: Record,
    modifier: Modifier = Modifier,
) {
    val fieldValues = getFieldValues(record) ?: emptyList()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 32.dp)
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        fieldValues.forEachIndexed { index, value ->
            if (value != null) {
                RarCell(
                    value = value,
                    index = index,
                    fontWeight = if (index == 0) FontWeight.SemiBold else null,
                )
            }
        }
    }
}

@Composable
fun RarCell(
    value: String,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.TopStart,
    color: Color = LocalContentColor.current,
    fontWeight: FontWeight? = null,
    isHeader: Boolean = false,
    index: Int,
) {

    val width = when (index) {
        0 -> 60.dp  // ID, so way smaller
        1 -> 150.dp // Status pill gets decent breathing room
        else -> 120.dp // Rate, Balance, Deposit flex wider
    }

    Box(
        modifier = modifier
            .heightIn(min = 40.dp)
            .width(width)
            .padding(end = 16.dp)
        ,
        contentAlignment = alignment
    ) {
        Text(
            text = value,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            color = color,
            fontWeight = fontWeight ?: FontWeight.Normal,
            style = if (isHeader) MaterialTheme.typography.labelMedium else MaterialTheme.typography.bodySmall,
        )
    }
}

fun getFieldNames(record: Record): List<String?>? = when (record) {
    is GiaRecord -> giaFieldNames
    is SetupRecord -> setupFieldNames
    else -> null
}

fun List<String?>?.mapToHeaders(record: Record): List<String>? {
    return when(record){
        is GiaRecord -> this?.map { name ->
            when(name){
                "id" -> "ID"
                "projectTitle" -> "Project Title"
                "beneficiary" -> "Beneficiary"
                "location" -> "Location"
                "projectDuration" -> "Project Duration"
                "projectCost" -> "Project Cost"
                "remarks" -> "Remarks"
                "className" -> "Class Name"
                else -> "Unknown Header"
            }
        }
        is SetupRecord -> this?.map { name ->
            when(name){
                "id" -> "ID"
                "firmName" -> "Firm Name"
                "proponent" -> "Proponent"
                "amountApproved" -> "Amount Approved"
                "yearApproved" -> "Year Approved"
                "location" -> "Location"
                "district" -> "District"
                "sector" -> "Sector"
                "status" -> "Status"
                "listOfEquipment" -> "List Of Equipment"
                else -> "Unknown Header"
            }
        }
        else -> emptyList()
    }
}

fun getFieldValues(record: Record): List<String?>? = getFieldNames(record)?.map { it?.let { name -> getFieldValue(record, name) } }