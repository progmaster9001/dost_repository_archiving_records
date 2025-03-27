package com.ojtapp.mobile

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.core.SheetDetent
import com.composables.core.rememberModalBottomSheetState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                isTableLayout = currentLayout == Layout.TABLE,
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

@Composable
fun TopToolbar(modifier: Modifier = Modifier, isTableLayout: Boolean, openFilterSheet: () -> Unit, setLayout: () -> Unit) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        IconButton(onClick = openFilterSheet) { Icon(imageVector = Icons.Default.Email, contentDescription = "file_icon")}
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