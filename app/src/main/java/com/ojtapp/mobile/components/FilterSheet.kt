package com.ojtapp.mobile.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ojtapp.mobile.components.util.FilterCriteria
import com.ojtapp.mobile.model.GiaClass
import com.ojtapp.mobile.components.util.GiaRecordFilterCriteria
import com.ojtapp.mobile.model.SectorType
import com.ojtapp.mobile.components.util.SetupRecordFilterCriteria
import com.ojtapp.mobile.model.Status
import com.ojtapp.mobile.model.Type
import com.ojtapp.mobile.components.util.isSame
import com.ojtapp.mobile.viewmodels.FilterEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    content: @Composable()(ColumnScope.() -> Unit)
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = Modifier.systemBarsPadding(),
        scrimColor = BottomSheetDefaults.ScrimColor.copy(alpha = .2f),
        sheetState = sheetState,
        shape = RectangleShape,
        dragHandle = null,
        content = content
    )
}

@Composable
fun FilterContent(
    currentTab: Type,
    giaFilterState: GiaRecordFilterCriteria,
    setupFilterState: SetupRecordFilterCriteria,
    onDismissRequest: () -> Unit,
    filterEvent: (FilterEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimensions.containerPadding)
            .imePadding(),
        contentAlignment = Alignment.TopEnd
    ) {
        when(currentTab){
            Type.GIA -> GiaFilterContent(giaFilterState, onDismissRequest, { filterEvent(FilterEvent.ResetFilter) }, { filterEvent(
                FilterEvent.ApplyFilter(it))} )
            Type.SETUP -> SetupFilterContent(setupFilterState, onDismissRequest, { filterEvent(
                FilterEvent.ResetFilter) },{ filterEvent(FilterEvent.ApplyFilter(it))} )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GiaFilterContent(
    giaFilterState: GiaRecordFilterCriteria,
    onDismissRequest: () -> Unit,
    resetFilter: () -> Unit,
    applyFilter: (FilterCriteria) -> Unit,
    modifier: Modifier = Modifier
) {
    var projectTitle by remember { mutableStateOf(giaFilterState.projectTitleContains ?: "") }
    var location by remember { mutableStateOf(giaFilterState.locationContains ?: "") }
    var classNameContains by remember { mutableStateOf(giaFilterState.classContains ?: "") }
    var beneficiaryContains by remember { mutableStateOf(giaFilterState.beneficiaryContains ?: "") }
    var remarksContains by remember { mutableStateOf(giaFilterState.remarksContains ?: "") }
    var minProjectCost by remember { mutableStateOf(giaFilterState.minProjectCost?.toString() ?: "") }
    var maxProjectCost by remember { mutableStateOf(giaFilterState.maxProjectCost?.toString() ?: "") }
    var selectedClasses by remember { mutableStateOf(giaFilterState.classesIn ?: emptyList()) }

    val scrollState = rememberScrollState()
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    val bringIntoViewRequester = remember { BringIntoViewRequester() }

    val isSame = giaFilterState.isSame(
        projectTitle, location, classNameContains, beneficiaryContains, remarksContains, minProjectCost, maxProjectCost, selectedClasses
    )
    val areFieldsNotEmpty = atLeastOneNotEmpty(
        projectTitle, location, classNameContains, beneficiaryContains, remarksContains, minProjectCost, maxProjectCost, selectedClasses
    )
    val activateReset = areFieldsNotEmpty && isSame

    val buttonColors = if (activateReset) {
        ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        )
    } else {
        ButtonDefaults.buttonColors(
            containerColor = Color(133, 224, 224, 255)
        )
    }

    val buttonText = if (activateReset) "Reset" else "Apply"

    Column(
        modifier = modifier.verticalScroll(scrollState)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Text("GIA Filter", style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "close_bottom_sheet",
                modifier = Modifier.clickable(onClick = onDismissRequest)
            )
        }

        OutlinedTextField(
            value = projectTitle,
            onValueChange = { projectTitle = it },
            label = { Text("Project Title") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged {
                    if (it.isFocused) {
                        coroutineScope.launch { bringIntoViewRequester.bringIntoView() }
                    }
                }
        )

        // --- Input fields ---
        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
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

        MultiSelectChipRow(
            title = "Select Multiple Classes",
            options = GiaClass.entries.map { it.value },
            selectedOptions = selectedClasses,
            onSelectionChanged = { selectedClasses = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (activateReset) {
                    projectTitle = ""
                    location = ""
                    classNameContains = ""
                    beneficiaryContains = ""
                    remarksContains = ""
                    minProjectCost = ""
                    maxProjectCost = ""
                    selectedClasses = emptyList()
                } else {
                    applyFilter(
                        GiaRecordFilterCriteria(
                            projectTitleContains = projectTitle.ifBlank { null },
                            locationContains = location.ifBlank { null },
                            classContains = classNameContains.ifBlank { null },
                            beneficiaryContains = beneficiaryContains.ifBlank { null },
                            remarksContains = remarksContains.ifBlank { null },
                            minProjectCost = minProjectCost.toDoubleOrNull(),
                            maxProjectCost = maxProjectCost.toDoubleOrNull(),
                            classesIn = selectedClasses.ifEmpty { null }
                        )
                    )
                }
            },
            colors = buttonColors,
            enabled = areFieldsNotEmpty || !isSame,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(buttonText, fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SetupFilterContent(
    setupFilterState: SetupRecordFilterCriteria,
    onDismissRequest: () -> Unit,
    resetFilter: () -> Unit,
    applyFilter: (FilterCriteria) -> Unit,
    modifier: Modifier = Modifier
) {

    var selectedSectors by remember { mutableStateOf(setupFilterState.sectorIn ?: emptyList()) }
    var selectedStatuses by remember { mutableStateOf(setupFilterState.statusIn ?: emptyList()) }

    var proponentContains by remember { mutableStateOf(setupFilterState.proponentContains ?: "") }
    var firmNameContains by remember { mutableStateOf(setupFilterState.firmNameContains ?: "") }

    var minAmountApproved by remember { mutableStateOf(setupFilterState.minAmountApproved?.toString() ?: "") }
    var maxAmountApproved by remember { mutableStateOf(setupFilterState.maxAmountApproved?.toString() ?: "") }

    val areFieldsNotEmpty = atLeastOneNotEmpty(selectedSectors, selectedStatuses, proponentContains, firmNameContains, minAmountApproved, maxAmountApproved)
    val isSame = setupFilterState.isSame(selectedSectors, selectedStatuses, proponentContains, firmNameContains, minAmountApproved, maxAmountApproved)
    val activateReset = areFieldsNotEmpty && isSame

    val buttonColors = when(activateReset){
        true -> ButtonDefaults.buttonColors().copy(containerColor = MaterialTheme.colorScheme.errorContainer, contentColor = MaterialTheme.colorScheme.onErrorContainer)
        false -> ButtonDefaults.buttonColors().copy(containerColor = Color(108, 172, 192, 255))
    }
    val text = when(activateReset){
        true -> "Reset"
        false -> "Apply"
    }

    val scrollState = rememberScrollState()
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    val bringIntoViewRequester = remember { BringIntoViewRequester() }

    Column(
        modifier = modifier
            .verticalScroll(scrollState),
    ) {
        Row(
            verticalAlignment = Alignment.Top
        ) {
            Text("SETUP Filter", style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
            Icon(imageVector = Icons.Default.Close, contentDescription = "close_bottom_sheet", modifier.clickable(onClick = onDismissRequest))
        }

        MultiSelectChipRow(
            title = "Select Multiple Sectors",
            options = SectorType.entries.map { it.value },
            selectedOptions = selectedSectors,
            onSelectionChanged = { selectedSectors = it }
        )


        MultiSelectChipRow(
            title = "Select Multiple Statuses",
            options = Status.entries.map { it.value },
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
        Button(
            onClick = {
                if(activateReset){
                    selectedSectors = emptyList()
                    selectedStatuses = emptyList()
                    proponentContains = ""
                    firmNameContains = ""
                    minAmountApproved = ""
                    maxAmountApproved = ""
                }else{
                    applyFilter(
                        SetupRecordFilterCriteria(
                            sectorIn = selectedSectors.ifEmpty { null },
                            statusIn = selectedStatuses.ifEmpty { null },
                            proponentContains = proponentContains.ifBlank { null },
                            firmNameContains = firmNameContains.ifBlank { null },
                            minAmountApproved = minAmountApproved.toDoubleOrNull(),
                            maxAmountApproved = maxAmountApproved.toDoubleOrNull()
                        )
                    )
                }
            },
            colors = buttonColors,
            enabled = areFieldsNotEmpty || !isSame,
            shape = RoundedCornerShape(20f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text, fontWeight = FontWeight.Bold)
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