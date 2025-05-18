package com.ojtapp.mobile.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ojtapp.mobile.components.util.FilterCriteria
import com.ojtapp.mobile.model.GiaClass
import com.ojtapp.mobile.components.util.GiaRecordFilterCriteria
import com.ojtapp.mobile.model.SectorType
import com.ojtapp.mobile.components.util.SetupRecordFilterCriteria
import com.ojtapp.mobile.model.Status
import com.ojtapp.mobile.model.Type
import com.ojtapp.mobile.components.util.isSame
import com.ojtapp.mobile.model.giaClassColors
import com.ojtapp.mobile.model.sectorTypeColors
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
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
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
        projectTitle, location, beneficiaryContains, remarksContains, minProjectCost, maxProjectCost, selectedClasses
    )
    val areFieldsNotEmpty = atLeastOneNotEmpty(
        projectTitle, location, beneficiaryContains, remarksContains, minProjectCost, maxProjectCost, selectedClasses
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 18.dp)
        ) {
            Text("GIA Filter", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "close_bottom_sheet",
                modifier = Modifier.clickable(onClick = onDismissRequest)
            )
        }
        HorizontalDivider()
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MultiSelectChipRow(
                title = "Select Multiple Classes",
                options = GiaClass.entries.map { it.value },
                selectedOptions = selectedClasses,
                onSelectionChanged = { selectedClasses = it }
            )

            HorizontalDivider()

            FilterTextField(
                title = "Project Title",
                value = projectTitle,
                placeHolder = "BioFloss",
                onValueChange = { projectTitle = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        if (it.isFocused) {
                            coroutineScope.launch { bringIntoViewRequester.bringIntoView() }
                        }
                    }
            )

            FilterTextField(
                title = "Beneficiary",
                value = beneficiaryContains,
                placeHolder = "Juan Dela Cruz",
                onValueChange = { beneficiaryContains = it }
            )

            FilterTextField(
                title = "Location",
                value = location,
                placeHolder = "Pagadian City",
                onValueChange = { location = it }
            )

            FilterTextField(
                title = "Remarks",
                value = location,
                placeHolder = "",
                onValueChange = { remarksContains = it }
            )

            FilterTextField(
                title = "Min Cost",
                value = minProjectCost,
                placeHolder = "0.0",
                onValueChange = { minProjectCost = it }
            )

            FilterTextField(
                title = "Max Cost",
                value = maxProjectCost,
                placeHolder = "0.0",
                onValueChange = { maxProjectCost = it }
            )

            Button(
                onClick = {
                    if (activateReset) {
                        projectTitle = ""
                        location = ""
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
                shape = RoundedCornerShape(20f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(buttonText, fontWeight = FontWeight.Bold)
            }
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
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 18.dp)
        ) {
            Text("SETUP Filter", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
            Icon(imageVector = Icons.Default.Close, contentDescription = "close_bottom_sheet", modifier.clickable(onClick = onDismissRequest))
        }
        HorizontalDivider()
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MultiSelectChipRow(
                title = "Select Multiple Sectors",
                options = SectorType.entries.map { it.value },
                selectedOptions = selectedSectors,
                onSelectionChanged = { selectedSectors = it }
            )

            HorizontalDivider()

            MultiSelectChipRow(
                title = "Select Multiple Statuses",
                options = Status.entries.map { it.value },
                selectedOptions = selectedStatuses,
                onSelectionChanged = { selectedStatuses = it }
            )

            HorizontalDivider()

            FilterTextField(
                title = "Firm",
                value = firmNameContains,
                placeHolder = "BioFloss",
                onValueChange = { firmNameContains = it }
            )

            FilterTextField(
                title = "Proponent",
                value = proponentContains,
                placeHolder = "Juan Dela Cruz",
                onValueChange = { proponentContains = it },
                modifier = Modifier.focusRequester(focusRequester)
                    .onFocusChanged { state ->
                        if (state.isFocused) {
                            coroutineScope.launch { bringIntoViewRequester.bringIntoView() }
                        }
                    }
            )

            FilterTextField(
                title = "Min Amount",
                value = minAmountApproved,
                placeHolder = "0.0",
                onValueChange = { minAmountApproved = it }
            )

            FilterTextField(
                title = "Max Amount",
                value = maxAmountApproved,
                placeHolder = "0.0",
                onValueChange = { maxAmountApproved = it }
            )

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
                    leadingIcon = {
                        (giaClassColors + sectorTypeColors).entries.find {
                            it.key == SectorType.from(option) || it.key == GiaClass.from(option)
                        }?.value?.let {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(it)
                            )
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    border = null,
                    colors = SelectableChipColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        labelColor = MaterialTheme.colorScheme.onSurface,
                        leadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        trailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,

                        disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.12f),
                        disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f),
                        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f),

                        selectedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        disabledSelectedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.12f),
                        selectedLabelColor = MaterialTheme.colorScheme.onSurface,
                        selectedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        selectedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
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

@Composable
fun FilterTextField(
    title: String,
    placeHolder: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val textStyle = MaterialTheme.typography.bodyMedium.copy(
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurface
    )

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        textStyle = textStyle.copy(
            fontWeight = FontWeight.Normal
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$title: ",
                    style = textStyle.copy(
                        textAlign = TextAlign.Right
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(.3f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Box(
                    modifier = Modifier.height(38.dp).weight(.7f).clip(OutlinedTextFieldDefaults.shape).background(
                        MaterialTheme.colorScheme.surfaceContainer
                    ),
                    contentAlignment = Alignment.CenterStart
                ){
                    Row {
                        Spacer(Modifier.width(12.dp))
                        Box{
                            if(value.isEmpty()){
                                Text(text = placeHolder, style = textStyle.copy(color = MaterialTheme.colorScheme.outline))
                            }
                            innerTextField()
                        }
                    }
                }
            }
        }
    )
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