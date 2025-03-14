package com.ojtapp.mobile

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.composables.core.ModalBottomSheet
import com.composables.core.ModalBottomSheetState
import com.composables.core.Scrim
import com.composables.core.Sheet

@Composable
fun FilterSheet(sheetState: ModalBottomSheetState, modifier: Modifier = Modifier, onDismissRequest: () -> Unit, content: @Composable()() -> Unit) {
    ModalBottomSheet(
        state = sheetState,
        onDismiss = {
            onDismissRequest()
        }
    ) {
        Scrim(
            enter = fadeIn(),
            exit = fadeOut()
        )
        Sheet(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(unbounded = true)
                .windowInsetsPadding(WindowInsets.systemBars)
                .imePadding()
                .clip(RoundedCornerShape(topStart = 100f, topEnd = 100f)))
        {
            Surface{
                content()
            }
        }
    }
}

@Composable
fun FilterContent(
    currentTab: Type,
    giaFilterState: GiaRecordFilterCriteria,
    setupFilterState: SetupRecordFilterCriteria,
    filterEvent: (FilterEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    when(currentTab){
        Type.GIA -> GiaFilterContent(giaFilterState, { filterEvent(FilterEvent.ResetFilter) }, { filterEvent(FilterEvent.ApplyFilter(it))} )
        Type.SETUP -> SetupFilterContent(setupFilterState,  { filterEvent(FilterEvent.ResetFilter) }, { filterEvent(FilterEvent.ApplyFilter(it))})
    }
}

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

    Column(
        modifier = modifier
            .padding(horizontal = Dimensions.horizontalPadding)
    ) {
        Text("GIA Filter Criteria", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            singleLine = true,
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = classNameContains,
            onValueChange = { classNameContains = it },
            label = { Text("Class Name Contains") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = beneficiaryContains,
            onValueChange = { beneficiaryContains = it },
            label = { Text("Beneficiary Contains") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = remarksContains,
            onValueChange = { remarksContains = it },
            label = { Text("Remarks Contains") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Row {
            OutlinedTextField(
                value = minProjectCost,
                onValueChange = { minProjectCost = it },
                label = { Text("Min Project Cost") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = maxProjectCost,
                onValueChange = { maxProjectCost = it },
                label = { Text("Max Project Cost") },
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
                modifier = Modifier.weight(1f)
            ) {
                Text("Reset")
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
                modifier = Modifier.weight(1f)
            ) {
                Text("Apply")
            }
        }
    }
}

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

    Column(
        modifier = modifier
            .padding(horizontal = Dimensions.horizontalPadding)
    ) {
        Text("SETUP Filter Criteria", style = MaterialTheme.typography.titleLarge)


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
            modifier = Modifier.fillMaxWidth()
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
                modifier = Modifier.weight(1f)
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
                modifier = Modifier.weight(1f)
            ) {
                Text("Reset")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
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
                modifier = Modifier.weight(1f)
            ) {
                Text("Apply")
            }
        }
    }
}

@Composable
fun MultiSelectChipRow(
    title: String,
    options: List<String>,
    selectedOptions: List<String>,
    onSelectionChanged: (List<String>) -> Unit
) {
    Column {
        Text(title, style = MaterialTheme.typography.titleMedium)
        LazyRow {
            items(options) { option ->
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
                    label = { Text(option) },
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}