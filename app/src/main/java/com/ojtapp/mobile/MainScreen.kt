package com.ojtapp.mobile

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.core.SheetDetent
import com.composables.core.rememberModalBottomSheetState
import kotlinx.coroutines.launch

@Composable
fun MainRoute(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    logout: () -> Unit
) {

    val user by viewModel.user.collectAsStateWithLifecycle()
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val currentLayout by viewModel.currentLayout.collectAsStateWithLifecycle()
    val recordsState by viewModel.filteredRecords.collectAsStateWithLifecycle()
    val giaRecordFilterState by viewModel.giaRecordFilter.collectAsStateWithLifecycle()
    val setupRecordFilterState by viewModel.setupRecordFilter.collectAsStateWithLifecycle()
    val dialogState by viewModel.dialogState.collectAsStateWithLifecycle()

    if(dialogState == DialogState.OPENED)
        WarningDialog(onDismissRequest = { viewModel.toggleDialog(DialogEvent.CloseDialog) },
            onLogout = viewModel::clearUser
        )

    LaunchedEffect(user) {
        if(user.token.isEmpty()) logout()
    }

    MainScreen(
        currentTab = currentTab,
        currentLayout = currentLayout,
        user = user,
        giaFilterState = giaRecordFilterState,
        setupFilterState = setupRecordFilterState,
        filterEvent = viewModel::filterEvent,
        recordsState = recordsState,
        toggleDialog = viewModel::toggleDialog,
        onSelectedTab = viewModel::setCurrentTab,
        setLayout = viewModel::setCurrentLayout
    )
}

@Composable
private fun MainScreen(
    modifier: Modifier = Modifier,
    currentTab: Type,
    currentLayout: Layout,
    user: User,
    giaFilterState: GiaRecordFilterCriteria,
    setupFilterState: SetupRecordFilterCriteria,
    filterEvent: (FilterEvent) -> Unit,
    recordsState: RecordState,
    toggleDialog: (DialogEvent) -> Unit,
    setLayout: () -> Unit,
    onSelectedTab: (Type) -> Unit
) {

    val sheetState = rememberModalBottomSheetState(
        initialDetent = SheetDetent.Hidden
    )
    val scope = rememberCoroutineScope()

    Scaffold{ innerPadding ->
        FilterSheet(
            sheetState = sheetState,
            onDismissRequest = { scope.launch { sheetState.animateTo(SheetDetent.Hidden) } }
        ) {
            FilterContent(currentTab, giaFilterState, setupFilterState,
                { event ->
                    filterEvent(event)
                    scope.launch {
                        sheetState.animateTo(SheetDetent.Hidden)
                    }
                }
            )
        }
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimensions.horizontalPadding),
                verticalAlignment = Alignment.CenterVertically
                )
            {
                Image(painter = painterResource(R.drawable.dost_seal), contentDescription = "dost_logo")
                Spacer(modifier = Modifier.width(Dimensions.basicSpacing))
                Text(
                    text = stringResource(R.string.welcome_user, user.name),
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.headlineMedium
                )
                IconButton(onClick = { toggleDialog(DialogEvent.OpenDialog) }) { Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "logout_icon")}
            }
            TypeTabRow(
                selectedTabIndex = currentTab.ordinal,
                onSelectedTab = onSelectedTab
            )
            TopToolbar(
                currentTab = currentTab,
                isTableLayout = currentLayout == Layout.TABLE,
                giaFilterState = giaFilterState,
                setupFilterState = setupFilterState,
                openFilterSheet = {  scope.launch { sheetState.animateTo(SheetDetent.FullyExpanded) }},
                setLayout = setLayout
            )
            RecordsContainer(
                currentLayout = currentLayout,
                recordsState = recordsState,
                setLayout = setLayout
            )
        }
    }
}

@Composable
fun RecordsContainer(
    currentLayout: Layout,
    recordsState: RecordState,
    setLayout: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        modifier = modifier
            .fillMaxSize(),
        targetState = recordsState
    ) { state ->
        when (state) {
            is RecordState.Error -> Text(text = state.error)
            RecordState.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            is RecordState.Success -> RecordLayout(currentLayout, state.records)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TopToolbar(
    currentTab: Type,
    modifier: Modifier = Modifier,
    giaFilterState: GiaRecordFilterCriteria,
    setupFilterState: SetupRecordFilterCriteria,
    isTableLayout: Boolean,
    openFilterSheet: () -> Unit,
    setLayout: () -> Unit
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Surface(
            contentColor = MaterialTheme.colorScheme.outline,
            modifier = Modifier.weight(1f)
        ) {
            CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.labelSmall) {
                Column(
                    modifier = Modifier.padding(Dimensions.containerPadding)
                ){
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
                                giaFilterState.location?.let { Text(
                                    text = buildAnnotatedString {
                                        withStyle(style = SpanStyle(
                                            fontWeight = FontWeight.Black
                                        )){
                                            append("Location: ")
                                        }
                                        append(it)
                                    },
                                ) }
                                giaFilterState.remarksContains?.let { Text(
                                    text = buildAnnotatedString {
                                        withStyle(style = SpanStyle(
                                            fontWeight = FontWeight.Black
                                        )){
                                            append("Remarks: ")
                                        }
                                        append(it)
                                    },
                                ) }
                                giaFilterState.classNameContains?.let { Text(
                                    text = buildAnnotatedString {
                                        withStyle(style = SpanStyle(
                                            fontWeight = FontWeight.Black
                                        )){
                                            append("Class Name: ")
                                        }
                                        append(it)
                                    },
                                ) }
                                giaFilterState.beneficiaryContains?.let { Text(
                                    text = buildAnnotatedString {
                                        withStyle(style = SpanStyle(
                                            fontWeight = FontWeight.Black
                                        )){
                                            append("Beneficiary: ")
                                        }
                                        append(it)
                                    },
                                ) }
                                giaFilterState.minProjectCost?.let { Text(
                                    text = buildAnnotatedString {
                                        withStyle(style = SpanStyle(
                                            fontWeight = FontWeight.Black
                                        )){
                                            append("Minimum Project Cost: ")
                                        }
                                        append("$it")
                                    },
                                ) }
                                giaFilterState.maxProjectCost?.let { Text(
                                    text = buildAnnotatedString {
                                        withStyle(style = SpanStyle(
                                            fontWeight = FontWeight.Black
                                        )){
                                            append("Max Project Cost: ")
                                        }
                                        append("$it")
                                    },
                                ) }
                                giaFilterState.projectDurationRange?.let { Text(
                                    text = buildAnnotatedString {
                                        withStyle(style = SpanStyle(
                                            fontWeight = FontWeight.Black
                                        )){
                                            append("Project Duration: ")
                                        }
                                        append("$it")
                                    },
                                ) }
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
                                    Row {
                                        Text(
                                            text = buildAnnotatedString {
                                                withStyle(style = SpanStyle(
                                                    fontWeight = FontWeight.Black
                                                )){
                                                    append("Selected Sectors: ")
                                                }
                                            },
                                        )
                                        FlowRow {
                                            it.forEachIndexed { index, value -> Text(if(it.size == (index + 1)) value else "$value, ") }
                                        }
                                    }
                                }
                                setupFilterState.statusIn?.let {
                                    Row {
                                        Text(
                                            text = buildAnnotatedString {
                                                withStyle(style = SpanStyle(
                                                    fontWeight = FontWeight.Black
                                                )){
                                                    append("Selected Statuses: ")
                                                }
                                            },
                                        )
                                        FlowRow {
                                            it.forEachIndexed { index, value -> Text(if(it.size == (index + 1)) value else "$value, ") }
                                        }
                                    }
                                }
                                setupFilterState.proponentContains?.let {
                                    Text(
                                        text = buildAnnotatedString {
                                            withStyle(style = SpanStyle(
                                                fontWeight = FontWeight.Black
                                            )){
                                                append("Proponent: ")
                                            }
                                            append(it)
                                        },
                                    )
                                }
                                setupFilterState.firmNameContains?.let {
                                    Text(
                                        text = buildAnnotatedString {
                                            withStyle(style = SpanStyle(
                                                fontWeight = FontWeight.Black
                                            )){
                                                append("Firm Name: ")
                                            }
                                            append(it)
                                        },
                                    )
                                }
                                setupFilterState.minYearApproved?.let {
                                    Text(
                                        text = buildAnnotatedString {
                                            withStyle(style = SpanStyle(
                                                fontWeight = FontWeight.Black
                                            )){
                                                append("Min year Approved: ")
                                            }
                                            append("$it")
                                        },
                                    )
                                }
                                setupFilterState.maxYearApproved?.let {
                                    Text(
                                        text = buildAnnotatedString {
                                            withStyle(style = SpanStyle(
                                                fontWeight = FontWeight.Black
                                            )){
                                                append("Max Year Approved: ")
                                            }
                                            append("$it")
                                        },
                                    )
                                }
                                setupFilterState.minAmountApproved?.let {
                                    Text(
                                        text = buildAnnotatedString {
                                            withStyle(style = SpanStyle(
                                                fontWeight = FontWeight.Black
                                            )){
                                                append("Min Amount Approved: ")
                                            }
                                            append("$it")
                                        },
                                    )
                                }
                                setupFilterState.maxAmountApproved?.let {
                                    Text(
                                        text = buildAnnotatedString {
                                            withStyle(style = SpanStyle(
                                                fontWeight = FontWeight.Black
                                            )){
                                                append("Max Amount Approved: ")
                                            }
                                            append("$it")
                                        },
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
        IconButton(onClick = openFilterSheet) { Icon(imageVector = Icons.Default.Email, contentDescription = "file_icon")}
        Spacer(Modifier.width(Dimensions.basicSpacing))
        LayoutSwitch(isTableLayout = isTableLayout, setLayout = setLayout)
    }
}

@Composable
fun RecordLayout(
    currentLayout: Layout,
    records: List<Record>,
    modifier: Modifier = Modifier
) {
    Column {
        AnimatedContent(targetState = currentLayout) { layout ->
            when(layout){
                Layout.CARD -> RecordCardLayout(records)
                Layout.TABLE -> RecordTableLayout(records)
            }
        }
    }
}