package com.ojtapp.mobile.screens

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ojtapp.mobile.R
import com.ojtapp.mobile.components.Dimensions
import com.ojtapp.mobile.components.FilterContent
import com.ojtapp.mobile.components.FilterSheet
import com.ojtapp.mobile.components.FilteredTextContent
import com.ojtapp.mobile.components.LayoutSwitch
import com.ojtapp.mobile.components.RarLoadingProgressIndicator
import com.ojtapp.mobile.components.RecordCardLayout
import com.ojtapp.mobile.components.RecordTableLayout
import com.ojtapp.mobile.components.TypeTabRow
import com.ojtapp.mobile.components.WarningDialog
import com.ojtapp.mobile.components.util.GiaRecordFilterCriteria
import com.ojtapp.mobile.components.util.SetupRecordFilterCriteria
import com.ojtapp.mobile.components.util.countFilters
import com.ojtapp.mobile.model.Record
import com.ojtapp.mobile.model.Type
import com.ojtapp.mobile.model.User
import com.ojtapp.mobile.viewmodels.DialogEvent
import com.ojtapp.mobile.viewmodels.DialogState
import com.ojtapp.mobile.viewmodels.FilterEvent
import com.ojtapp.mobile.viewmodels.Layout
import com.ojtapp.mobile.viewmodels.MainViewModel
import com.ojtapp.mobile.viewmodels.RecordState

@Composable
fun MainRoute(
    viewModel: MainViewModel,
    logout: () -> Unit,
    onFileClick: () -> Unit
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
        setLayout = viewModel::setCurrentLayout,
        onFileClick = onFileClick,
        onSelectedTab = viewModel::setCurrentTab,
    )
}

val LocalRecordTab = staticCompositionLocalOf { Type.GIA }

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
    onFileClick: () -> Unit,
    onSelectedTab: (Type) -> Unit
) {

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            AppHeader(
                user = user,
                currentTab = currentTab,
                giaFilterState = giaFilterState,
                setupFilterState = setupFilterState,
                resetFilter = { filterEvent(FilterEvent.ResetFilter) },
                toggleDialog = toggleDialog,
            )
        },
        floatingActionButton = {
            TypeTabRow(
                filterAmount = countFilters(currentTab, giaFilterState, setupFilterState),
                selectedTabIndex = currentTab.ordinal,
                onFilterSelect = { showBottomSheet = true },
                onSelectedTab = onSelectedTab,
                onFileClick = onFileClick
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceBright,
        floatingActionButtonPosition = FabPosition.Center
    ){ innerPadding ->
        if(showBottomSheet){
            FilterSheet(
                sheetState = sheetState,
                onDismissRequest = {
                    showBottomSheet = false
                }
            ) {
                FilterContent(
                    currentTab = currentTab,
                    giaFilterState = giaFilterState,
                    setupFilterState = setupFilterState,
                    onDismissRequest = { showBottomSheet = false },
                    filterEvent = { event ->
                        filterEvent(event)
                        showBottomSheet = false
                    }
                )
            }
        }

        Box(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(innerPadding).fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val records = when(recordsState){
                        is RecordState.Error -> emptyList()
                        RecordState.Loading -> emptyList()
                        is RecordState.Success -> recordsState.records
                    }
                    Text(
                        "Records Found: ${records.size}",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.labelMedium
                    )
                    LayoutSwitch(
                        firstText = "Table",
                        secondText = "Card",
                        isTableLayout = currentLayout == Layout.TABLE,
                        setLayout = setLayout,
                    )
                }
                RecordsContainer(currentLayout = currentLayout, recordsState = recordsState)
            }
        }
    }
}

@Composable
fun AppHeader(
    modifier: Modifier = Modifier,
    user: User,
    currentTab: Type,
    giaFilterState: GiaRecordFilterCriteria,
    setupFilterState: SetupRecordFilterCriteria,
    resetFilter: () -> Unit,
    toggleDialog: (DialogEvent) -> Unit) {
//    Box(
//        modifier = Modifier
//            .shadowWithClipIntersect(
//                elevation = 4.dp,
//                shape = RectangleShape
//            )
//            .background(MaterialTheme.colorScheme.surfaceBright)
//    ){
//
//    }
    Box(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceBright)){
        Column(
            modifier = Modifier
                .then(
                    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
                        Modifier.statusBarsPadding()
                    } else {
                        Modifier.safeDrawingPadding()
                    }
                )
                .animateContentSize()
        ) {
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
                Text(
                    text = "Leave",
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = { toggleDialog(DialogEvent.OpenDialog) }
                    ),
                    color = MaterialTheme.colorScheme.error
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            FilteredTextContent(
                currentTab = currentTab,
                giaFilterState = giaFilterState,
                setupFilterState = setupFilterState,
                resetFilter = resetFilter
            )
        }
    }
}

@Composable
fun RecordsContainer(
    currentLayout: Layout,
    recordsState: RecordState
) {
    AnimatedContent(
        targetState = recordsState,
    ) { state ->
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            when (state) {
                is RecordState.Error -> Text(text = state.error, textAlign = TextAlign.Center)
                RecordState.Loading -> RarLoadingProgressIndicator()
                is RecordState.Success -> RecordLayout(currentLayout, state.records, Modifier.align(Alignment.TopStart))
            }
        }
    }
}

@Composable
fun RecordLayout(
    currentLayout: Layout,
    records: List<Record>,
    modifier: Modifier = Modifier
) {
    AnimatedContent(modifier = modifier, targetState = currentLayout) { layout ->
        when(layout){
            Layout.CARD -> RecordCardLayout(records)
            Layout.TABLE -> RecordTableLayout(records)
        }
    }
}

fun Modifier.shadowWithClipIntersect(
    elevation: Dp,
    shape: Shape = RectangleShape,
    clip: Boolean = elevation > 0.dp,
    ambientColor: Color = DefaultShadowColor,
    spotColor: Color = DefaultShadowColor,
): Modifier = this
    .drawWithCache {
        val bottomOffsetPx = elevation.toPx() * 2.2f
        val adjustedSize = Size(size.width, size.height + bottomOffsetPx)
        val outline = shape.createOutline(adjustedSize, layoutDirection, this)

        val path = Path().apply { addOutline(outline) }
        onDrawWithContent {
            clipPath(path, ClipOp.Intersect) {
                this@onDrawWithContent.drawContent()
            }
        }
    }
    .shadow(elevation, shape, clip, ambientColor, spotColor)