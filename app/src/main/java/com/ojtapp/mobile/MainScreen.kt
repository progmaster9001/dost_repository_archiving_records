package com.ojtapp.mobile

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun MainRoute(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    logout: () -> Unit
) {

    val user by viewModel.user.collectAsStateWithLifecycle()
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val currentLayout by viewModel.currentLayout.collectAsStateWithLifecycle()
    val recordsState by viewModel.records.collectAsStateWithLifecycle()
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
    recordsState: RecordState,
    toggleDialog: (DialogEvent) -> Unit,
    setLayout: () -> Unit,
    onSelectedTab: (Type) -> Unit
) {
    var shouldShowFilterSheet by remember { mutableStateOf(false) }
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        floatingActionButton = {
            FloatingActionButton(onClick = { shouldShowFilterSheet = !shouldShowFilterSheet }) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "drawer_fab"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(Dimensions.containerPadding)
                .padding(innerPadding)
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimensions.horizontalPadding), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.welcome_user, user.name),
                    style = MaterialTheme.typography.bodyLarge
                )
                IconButton(onClick = { toggleDialog(DialogEvent.OpenDialog) }) { Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "logout_icon")}
            }
            TypeTabRow(
                selectedTabIndex = currentTab.ordinal,
                onSelectedTab = onSelectedTab
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
            is RecordState.Success -> RecordLayout(currentLayout, state.records, setLayout)
        }
    }
}

@Composable
fun RecordLayout(
    currentLayout: Layout,
    records: List<Record>,
    setLayout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        IconButton(onClick = setLayout) { Icon(imageVector = Icons.Default.Refresh, contentDescription = "layout_icon") }
        AnimatedContent(targetState = currentLayout) { layout ->
            when(layout){
                Layout.CARD -> RecordCardLayout(records)
                Layout.TABLE -> RecordTableLayout(records)
            }
        }
    }
}