package com.ojtapp.mobile

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch

@Composable
fun MainRoute(
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

@OptIn(ExperimentalMaterial3Api::class)
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

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets.safeDrawing,
        floatingActionButton = {
            TypeTabRow(
                filterAmount = countFilters(currentTab, giaFilterState, setupFilterState),
                selectedTabIndex = currentTab.ordinal,
                onFilterSelect = { showBottomSheet = true },
                onSelectedTab = onSelectedTab
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ){ innerPadding ->
        if(showBottomSheet){
            FilterSheet(
                sheetState = sheetState,
                onDismissRequest = { showBottomSheet = false }
            ) {
                FilterContent(currentTab, giaFilterState, setupFilterState,
                    { event ->
                        filterEvent(event)
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if(!sheetState.isVisible){
                                showBottomSheet = false
                            }
                        }
                    }
                )
            }
        }
        Column(modifier = Modifier.padding(innerPadding)
        ) {
            AppHeader(user = user, toggleDialog = toggleDialog)
            TopToolbar(
                currentTab = currentTab,
                isTableLayout = currentLayout == Layout.TABLE,
                giaFilterState = giaFilterState,
                setupFilterState = setupFilterState,
                setLayout = setLayout
            )
            RecordsContainer(currentLayout = currentLayout, recordsState = recordsState,)
        }
    }
}

@Composable
fun AppHeader(modifier: Modifier = Modifier, user: User, toggleDialog: (DialogEvent) -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Dimensions.horizontalPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.dost_seal),
            contentDescription = "dost_logo",
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(Dimensions.basicSpacing))
        Text(
            text = stringResource(R.string.welcome_user, user.name),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
        IconButton(
            onClick = { toggleDialog(DialogEvent.OpenDialog) }
        ) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "logout_icon")
        }
    }
}

@Composable
fun RecordsContainer(
    currentLayout: Layout,
    recordsState: RecordState,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        modifier = modifier
            .fillMaxSize(),
        targetState = recordsState
    ) { state ->
        when (state) {
            is RecordState.Error -> Text(text = state.error)
            RecordState.Loading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            is RecordState.Success -> RecordLayout(currentLayout, state.records)
        }
    }
}

@Composable
fun TopToolbar(
    currentTab: Type,
    modifier: Modifier = Modifier,
    giaFilterState: GiaRecordFilterCriteria,
    setupFilterState: SetupRecordFilterCriteria,
    isTableLayout: Boolean,
    setLayout: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        FilteredTextContent(
            currentTab = currentTab,
            giaFilterState = giaFilterState,
            setupFilterState = setupFilterState
        )
        LayoutSwitch(isTableLayout = isTableLayout, setLayout = setLayout)
    }
}

@Composable
fun RecordLayout(
    currentLayout: Layout,
    records: List<Record>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        AnimatedContent(targetState = currentLayout) { layout ->
            when(layout){
                Layout.CARD -> RecordCardLayout(records)
                Layout.TABLE -> RecordTableLayout(records)
            }
        }
    }
}