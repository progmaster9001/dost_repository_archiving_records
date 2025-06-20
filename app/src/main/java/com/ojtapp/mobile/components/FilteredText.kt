package com.ojtapp.mobile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.ojtapp.mobile.components.util.GiaRecordFilterCriteria
import com.ojtapp.mobile.components.util.SetupRecordFilterCriteria
import com.ojtapp.mobile.model.Type
import com.ojtapp.mobile.components.util.isEmpty

@Composable
fun FilteredTextContent(
    modifier: Modifier = Modifier,
    currentTab: Type,
    giaFilterState: GiaRecordFilterCriteria,
    setupFilterState: SetupRecordFilterCriteria,
    resetFilter: () -> Unit
) {
    if(currentTab == Type.GIA && !giaFilterState.isEmpty() || currentTab == Type.SETUP && !setupFilterState.isEmpty()){
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .padding(horizontal = Dimensions.horizontalPadding)
                .background(MaterialTheme.colorScheme.surfaceBright)
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.labelSmall.copy(
                    textAlign = TextAlign.Right
                )
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                ){
                    when(currentTab){
                        Type.GIA -> {
                            if(
                                giaFilterState.projectTitleContains != null ||
                                giaFilterState.locationContains != null ||
                                giaFilterState.classContains != null ||
                                giaFilterState.beneficiaryContains != null ||
                                giaFilterState.remarksContains != null ||
                                giaFilterState.minProjectCost != null ||
                                giaFilterState.maxProjectCost != null ||
                                giaFilterState.classesIn != null
                            ) {
                                giaFilterState.projectTitleContains?.let {
                                    SingleFilterText(
                                        name = "Project Title",
                                        text = it
                                    )
                                }
                                giaFilterState.locationContains?.let {
                                    SingleFilterText(
                                        name = "Location",
                                        text = it
                                    )
                                }
                                giaFilterState.remarksContains?.let {
                                    SingleFilterText(
                                        name = "Remarks",
                                        text = it
                                    )
                                }
                                giaFilterState.classContains?.let {
                                    SingleFilterText(
                                        name = "Class",
                                        text = it
                                    )
                                }
                                giaFilterState.beneficiaryContains?.let {
                                    SingleFilterText(
                                        name = "Beneficiary",
                                        text = it
                                    )
                                }
                                giaFilterState.minProjectCost?.let {
                                    SingleFilterText(
                                        name = "Min Project Cost",
                                        text = it.toString()
                                    )
                                }
                                giaFilterState.maxProjectCost?.let {
                                    SingleFilterText(
                                        name = "Max Project Cost",
                                        text = it.toString()
                                    )
                                }
                                giaFilterState.classesIn?.let {
                                    MultiFilterText(
                                        name = "Selected Classes",
                                        values = it
                                    )
                                }
                            }
                        }
                        Type.SETUP -> {
                            if(
                                setupFilterState.statusIn != null ||
                                setupFilterState.sectorIn != null ||
                                setupFilterState.sectorContains != null ||
                                setupFilterState.firmNameContains != null ||
                                setupFilterState.proponentContains != null ||
                                setupFilterState.minAmountApproved != null ||
                                setupFilterState.maxAmountApproved != null
                            )
                            {
                                setupFilterState.sectorIn?.let {
                                    MultiFilterText(
                                        name = "Selected Sectors",
                                        values = it
                                    )
                                }
                                setupFilterState.statusIn?.let {
                                    MultiFilterText(
                                        name = "Selected Statuses",
                                        values = it
                                    )
                                }
                                setupFilterState.proponentContains?.let {
                                    SingleFilterText(
                                        name = "Proponent",
                                        text = it
                                    )
                                }
                                setupFilterState.firmNameContains?.let {
                                    SingleFilterText(
                                        name = "Firm Name",
                                        text = it
                                    )
                                }
                                setupFilterState.minAmountApproved?.let {
                                    SingleFilterText(
                                        name = "Min Amount Approved",
                                        text = it.toString()
                                    )
                                }
                                setupFilterState.maxAmountApproved?.let {
                                    SingleFilterText(
                                        name = "Max Amount Approved",
                                        text = it.toString()
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.width(Dimensions.basicSpacing))
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "reset_filter_icon",
                modifier = Modifier.size(28.dp).padding(4.dp).clip(CircleShape).clickable(onClick = resetFilter).align(Alignment.Top)
            )
        }
    }
}

@Composable
fun MultiFilterText(
    modifier: Modifier = Modifier,
    name: String,
    values: List<String>
) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(
                fontWeight = FontWeight.Black
            )
            ){
                append("$name: ")
            }
            values.forEachIndexed { index, value -> append(if(values.size == (index + 1)) value else "$value, ") }
        },
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun SingleFilterText(
    modifier: Modifier = Modifier,
    name: String,
    text: String
) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(
                fontWeight = FontWeight.Black
            )
            ){
                append("$name: ")
            }
            append(text)
        },
    )
}