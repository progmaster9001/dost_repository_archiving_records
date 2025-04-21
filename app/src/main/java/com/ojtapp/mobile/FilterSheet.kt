package com.ojtapp.mobile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    content: @Composable ()(ColumnScope.() -> Unit)
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = Modifier.systemBarsPadding(),
        scrimColor = BottomSheetDefaults.ScrimColor.copy(alpha = .2f),
        sheetState = sheetState,
        dragHandle = null,
        content = content
    )
}

@Composable
fun FilterContent(
    currentTab: Type,
    giaFilterState: GiaRecordFilterCriteria,
    setupFilterState: SetupRecordFilterCriteria,
    filterEvent: (FilterEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimensions.verticalPadding)
            .imePadding()
    ) {
        when(currentTab){
            Type.GIA -> GiaFilterContent(giaFilterState, { filterEvent(FilterEvent.ResetFilter) }, { filterEvent(FilterEvent.ApplyFilter(it))} )
            Type.SETUP -> SetupFilterContent(setupFilterState,  { filterEvent(FilterEvent.ResetFilter) }, { filterEvent(FilterEvent.ApplyFilter(it))} )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GiaFilterContent(
    giaFilterState: GiaRecordFilterCriteria,
    resetFilter: () -> Unit,
    applyFilter: (FilterCriteria) -> Unit,
    modifier: Modifier = Modifier
) {

    var location by remember { mutableStateOf(giaFilterState.location ?: "") }
    var classNameContains by remember { mutableStateOf(giaFilterState.classNameContains ?: "") }
    var beneficiaryContains by remember { mutableStateOf(giaFilterState.beneficiaryContains ?: "") }
    var remarksContains by remember { mutableStateOf(giaFilterState.remarksContains ?: "") }
    var minProjectCost by remember { mutableStateOf(giaFilterState.minProjectCost?.toString() ?: "") }
    var maxProjectCost by remember { mutableStateOf(giaFilterState.maxProjectCost?.toString() ?: "") }

    val scrollState = rememberScrollState()
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    val bringIntoViewRequester = remember { BringIntoViewRequester() }

    Column(
        modifier = modifier
            .padding(horizontal = Dimensions.horizontalPadding)
            .verticalScroll(scrollState)
    ) {
        Text("GIA Filter Criteria", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            singleLine = true,
            label = { Text("Location", maxLines = 1) },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged {
                    if (it.isFocused) {
                        coroutineScope.launch { bringIntoViewRequester.bringIntoView() }
                    }
                }
        )

        OutlinedTextField(
            value = classNameContains,
            onValueChange = { classNameContains = it },
            label = { Text("Class Name Contains", maxLines = 1) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = beneficiaryContains,
            onValueChange = { beneficiaryContains = it },
            label = { Text("Beneficiary Contains", maxLines = 1) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = remarksContains,
            onValueChange = { remarksContains = it },
            label = { Text("Remarks Contains", maxLines = 1) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Row {
            OutlinedTextField(
                value = minProjectCost,
                onValueChange = { minProjectCost = it },
                label = { Text("Min Project Cost", maxLines = 1) },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = maxProjectCost,
                onValueChange = { maxProjectCost = it },
                label = { Text("Max Project Cost", maxLines = 1) },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            TextButton(
                onClick = {
                    resetFilter()
                    location = ""
                    classNameContains = ""
                    beneficiaryContains = ""
                    remarksContains = ""
                    minProjectCost = ""
                    maxProjectCost = ""
                },
                colors = ButtonDefaults.textButtonColors().copy(contentColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.weight(1f)
            ) {
                Text("Reset", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    applyFilter(
                        GiaRecordFilterCriteria(
                            location = location.ifBlank { null },
                            classNameContains = classNameContains.ifBlank { null },
                            beneficiaryContains = beneficiaryContains.ifBlank { null },
                            remarksContains = remarksContains.ifBlank { null },
                            minProjectCost = minProjectCost.toIntOrNull(),
                            maxProjectCost = maxProjectCost.toIntOrNull()
                        )
                    )
                },
                enabled = atLeastOneNotEmpty(location, classNameContains, beneficiaryContains, remarksContains, minProjectCost, maxProjectCost),
                colors = ButtonDefaults.buttonColors().copy(containerColor = Color(133, 224, 224, 255)),
                shape = RoundedCornerShape(20f),
                modifier = Modifier.weight(1f)
            ) {
                Text("Apply", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SetupFilterContent(
    setupFilterState: SetupRecordFilterCriteria,
    resetFilter: () -> Unit,
    applyFilter: (FilterCriteria) -> Unit,
    modifier: Modifier = Modifier
) {

    var selectedSectors by remember { mutableStateOf(setupFilterState.sectorNameIn ?: emptyList()) }
    var selectedStatuses by remember { mutableStateOf(setupFilterState.statusIn ?: emptyList()) }

    var proponentContains by remember { mutableStateOf(setupFilterState.proponentContains ?: "") }
    var firmNameContains by remember { mutableStateOf(setupFilterState.firmNameContains ?: "") }

    var minYearApproved by remember { mutableStateOf(setupFilterState.minYearApproved?.toString() ?: "") }
    var maxYearApproved by remember { mutableStateOf(setupFilterState.maxYearApproved?.toString() ?: "") }

    var minAmountApproved by remember { mutableStateOf(setupFilterState.minAmountApproved?.toString() ?: "") }
    var maxAmountApproved by remember { mutableStateOf(setupFilterState.maxAmountApproved?.toString() ?: "") }

    val scrollState = rememberScrollState()
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    val bringIntoViewRequester = remember { BringIntoViewRequester() }

    Column(
        modifier = modifier
            .padding(horizontal = Dimensions.horizontalPadding)
            .verticalScroll(scrollState)
    ) {
        Text(
            "SETUP Filter Criteria",
            style = MaterialTheme.typography.titleLarge
        )

        MultiSelectChipRow(
            title = "Select Multiple Sectors",
            options = listOf("Agriculture", "Tech", "Health", "Finance"), // replace with your actual sectors
            selectedOptions = selectedSectors,
            onSelectionChanged = { selectedSectors = it }
        )


        MultiSelectChipRow(
            title = "Select Multiple Statuses",
            options = listOf("Approved", "Pending", "Rejected"), // replace with your actual statuses
            selectedOptions = selectedStatuses,
            onSelectionChanged = { selectedStatuses = it }
        )

        OutlinedTextField(
            value = proponentContains,
            onValueChange = { proponentContains = it },
            label = { Text("Proponent Contains") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { state ->
                    if (state.isFocused) {
                        coroutineScope.launch { bringIntoViewRequester.bringIntoView() }
                    }
                }
        )

        OutlinedTextField(
            value = firmNameContains,
            onValueChange = { firmNameContains = it },
            label = { Text("Firm Name Contains") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Row {
            OutlinedTextField(
                value = minYearApproved,
                onValueChange = { minYearApproved = it },
                label = { Text("Min Year Approved", maxLines = 1) },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = maxYearApproved,
                onValueChange = { maxYearApproved = it },
                label = { Text("Max Year Approved", maxLines = 1) },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
        }

        Row {
            OutlinedTextField(
                value = minAmountApproved,
                onValueChange = { minAmountApproved = it },
                label = { Text("Min Amount Approved", maxLines = 1) },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = maxAmountApproved,
                onValueChange = { maxAmountApproved = it },
                label = { Text("Max Amount Approved", maxLines = 1) },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            TextButton(
                onClick = {
                    resetFilter()
                    selectedSectors = emptyList()
                    selectedStatuses = emptyList()
                    proponentContains = ""
                    firmNameContains = ""
                    minYearApproved = ""
                    maxYearApproved = ""
                    minAmountApproved = ""
                    maxAmountApproved = ""
                },
                colors = ButtonDefaults.textButtonColors().copy(contentColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.weight(1f)
            ) {
                Text("Reset", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                enabled = atLeastOneNotEmpty(selectedSectors, selectedStatuses, proponentContains, firmNameContains, minYearApproved, maxYearApproved, minAmountApproved, maxAmountApproved),
                onClick = {
                    applyFilter(
                        SetupRecordFilterCriteria(
                            sectorNameIn = selectedSectors.ifEmpty { null },
                            statusIn = selectedStatuses.ifEmpty { null },
                            proponentContains = proponentContains.ifBlank { null },
                            firmNameContains = firmNameContains.ifBlank { null },
                            minYearApproved = minYearApproved.toIntOrNull(),
                            maxYearApproved = maxYearApproved.toIntOrNull(),
                            minAmountApproved = minAmountApproved.toIntOrNull(),
                            maxAmountApproved = maxAmountApproved.toIntOrNull()
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors().copy(containerColor = Color(133, 224, 224, 255)),
                shape = RoundedCornerShape(20f),
                modifier = Modifier.weight(1f)
            ) {
                Text("Apply", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MultiSelectChipRow(
    title: String,
    options: List<String>,
    selectedOptions: List<String>,
    onSelectionChanged: (List<String>) -> Unit
) {
    Column {
        Text(
            title,
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.titleSmall
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ){
            options.forEach { option ->
                val selected = option in selectedOptions
                FilterChip(
                    selected = selected,
                    onClick = {
                        val newSelection = if (selected) {
                            selectedOptions - option
                        } else {
                            selectedOptions + option
                        }
                        onSelectionChanged(newSelection)
                    },
                    label = { Text(option) }
                )
            }
        }
    }
}

fun atLeastOneNotEmpty(vararg values: Any?): Boolean {
    return values.any { value ->
        when (value) {
            is String -> value.isNotBlank()
            is List<*> -> value.any { (it as? String)?.isNotBlank() == true }
            else -> false
        }
    }
}