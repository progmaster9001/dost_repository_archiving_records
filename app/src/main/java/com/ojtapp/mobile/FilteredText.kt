package com.ojtapp.mobile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle

@Composable
fun FilteredTextContent(
    modifier: Modifier = Modifier,
    currentTab: Type,
    giaFilterState: GiaRecordFilterCriteria,
    setupFilterState: SetupRecordFilterCriteria
) {
    Surface(
        contentColor = MaterialTheme.colorScheme.outline,
        modifier = modifier
    ) {
        CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.labelSmall) {
            Column{
                when(currentTab){
                    Type.GIA -> {
                        if(
                            giaFilterState.location != null ||
                            giaFilterState.classNameContains != null ||
                            giaFilterState.beneficiaryContains != null ||
                            giaFilterState.remarksContains != null ||
                            giaFilterState.minProjectCost != null ||
                            giaFilterState.maxProjectCost != null ||
                            giaFilterState.projectDurationRange != null
                        )
                        {
                            giaFilterState.location?.let {
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
                            giaFilterState.classNameContains?.let {
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
                            giaFilterState.projectDurationRange?.let {
                                SingleFilterText(
                                    name = "Project Duration Range",
                                    text = it.toString()
                                )
                            }
                        }else{
                            Text("No filter applied.")
                        }
                    }
                    Type.SETUP -> {
                        if(
                            setupFilterState.statusIn != null ||
                            setupFilterState.sectorNameIn != null ||
                            setupFilterState.sectorName != null ||
                            setupFilterState.firmNameContains != null ||
                            setupFilterState.proponentContains != null ||
                            setupFilterState.minYearApproved != null ||
                            setupFilterState.maxYearApproved != null ||
                            setupFilterState.minAmountApproved != null ||
                            setupFilterState.maxAmountApproved != null
                        )
                        {
                            setupFilterState.sectorNameIn?.let {
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
                            setupFilterState.minYearApproved?.let {
                                SingleFilterText(
                                    name = "Min Year Approved",
                                    text = it.toString()
                                )
                            }
                            setupFilterState.maxYearApproved?.let {
                                SingleFilterText(
                                    name = "Max Year Approved",
                                    text = it.toString()
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
                        }else{
                            Text("No filter applied.")
                        }
                    }
                }
            }
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