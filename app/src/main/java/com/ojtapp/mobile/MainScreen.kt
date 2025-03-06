package com.ojtapp.mobile

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
        user = user,
        recordsState = recordsState,
        toggleDialog = viewModel::toggleDialog,
        onSelectedTab = viewModel::setCurrentTab
    )

}

@Composable
private fun MainScreen(
    modifier: Modifier = Modifier,
    currentTab: Type,
    user: User,
    recordsState: RecordState,
    toggleDialog: (DialogEvent) -> Unit,
    onSelectedTab: (Type) -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(Dimensions.containerPadding).padding(innerPadding)
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = Dimensions.horizontalPadding), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
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
                currentTab = currentTab,
                recordsState = recordsState
            )
        }
    }
}

@Composable
fun RecordsContainer(
    currentTab: Type,
    recordsState: RecordState,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        modifier = modifier.fillMaxSize(),
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
            is RecordState.Success -> RecordsList(
                currentTab = currentTab,
                records = state.records
            )
        }
    }
}

@Composable
fun RecordsList(
    currentTab: Type,
    records: List<Record>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(horizontal = Dimensions.horizontalPadding),
        verticalArrangement = Arrangement.spacedBy(Dimensions.basicSpacing)
    ) {
        item { Spacer(Modifier.height(Dimensions.basicSpacing)) }
        items(records.filter {
            (currentTab == Type.GIA && it is GiaRecord) || (currentTab == Type.SETUP && it is SetupRecord)
        }) { record ->
            val text = when (record) {
                is GiaRecord -> record.projectTitle
                is SetupRecord -> record.firmName
                else -> "Unknown Record"
            }
            Text(text = text)
        }
        item { Spacer(Modifier.height(Dimensions.basicSpacing)) }
    }
}