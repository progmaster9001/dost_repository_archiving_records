package com.ojtapp.mobile.screens

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aay.compose.baseComponents.model.LegendPosition
import com.aay.compose.donutChart.PieChart
import com.aay.compose.donutChart.model.PieChartData
import com.ojtapp.mobile.R
import com.ojtapp.mobile.components.Dimensions
import com.ojtapp.mobile.components.FilterContent
import com.ojtapp.mobile.components.FilterSheet
import com.ojtapp.mobile.components.FilteredTextContent
import com.ojtapp.mobile.components.LayoutSwitch
import com.ojtapp.mobile.components.RarDrawer
import com.ojtapp.mobile.components.RarLoadingProgressIndicator
import com.ojtapp.mobile.components.RecordCardLayout
import com.ojtapp.mobile.components.RecordTableLayout
import com.ojtapp.mobile.components.SelectedRecordDialog
import com.ojtapp.mobile.components.TypeTabRow
import com.ojtapp.mobile.components.WarningDialog
import com.ojtapp.mobile.components.YearDropdownMenu
import com.ojtapp.mobile.components.util.GiaRecordFilterCriteria
import com.ojtapp.mobile.components.util.SetupRecordFilterCriteria
import com.ojtapp.mobile.components.util.countFilters
import com.ojtapp.mobile.model.GiaClass
import com.ojtapp.mobile.model.GiaRecord
import com.ojtapp.mobile.model.Record
import com.ojtapp.mobile.model.SectorType
import com.ojtapp.mobile.model.SetupRecord
import com.ojtapp.mobile.model.Type
import com.ojtapp.mobile.model.User
import com.ojtapp.mobile.model.giaClassColors
import com.ojtapp.mobile.model.sectorTypeColors
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
    onRecordClick: (String) -> Unit,
    onFileClick: () -> Unit
) {

    val user by viewModel.user.collectAsStateWithLifecycle()
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val currentLayout by viewModel.currentLayout.collectAsStateWithLifecycle()
    val recordsState by viewModel.filteredRecords.collectAsStateWithLifecycle()
    val giaRecordFilterState by viewModel.giaRecordFilter.collectAsStateWithLifecycle()
    val setupRecordFilterState by viewModel.setupRecordFilter.collectAsStateWithLifecycle()
    val dialogState by viewModel.dialogState.collectAsStateWithLifecycle()
    val selectedYear by viewModel.selectedYear.collectAsStateWithLifecycle()
    val selectedRecord by viewModel.selectedRecord.collectAsStateWithLifecycle()
    var doesRecordExist by remember { mutableStateOf(false) }

    if(doesRecordExist){
        SelectedRecordDialog(
            title = selectedRecord?.first ?: "",
            onDismissRequest = {
                doesRecordExist = false
                viewModel.updateRecord(it)
            },
            onClick = {
                doesRecordExist = false
                onRecordClick(selectedRecord?.second ?: "")
                viewModel.updateRecord(null)
            }
        )
    }

    if(dialogState == DialogState.OPENED)
        WarningDialog(onDismissRequest = { viewModel.toggleDialog(DialogEvent.CloseDialog) },
            onLogout = viewModel::clearUser
        )

    LaunchedEffect(user) {
        if(user.token.isEmpty()) logout()
    }

    MainScreen(
        currentTab = currentTab,
        selectedYear = selectedYear,
        currentLayout = currentLayout,
        user = user,
        giaFilterState = giaRecordFilterState,
        setupFilterState = setupRecordFilterState,
        filterEvent = viewModel::filterEvent,
        recordsState = recordsState,
        toggleDialog = viewModel::toggleDialog,
        setLayout = viewModel::setCurrentLayout,
        onRecordClick = {
            doesRecordExist = true
            viewModel.updateRecord(it)
        },
        onFileClick = onFileClick,
        selectYear = viewModel::updateYear,
        onSelectedTab = viewModel::setCurrentTab,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreen(
    modifier: Modifier = Modifier,
    currentTab: Type,
    selectedYear: Int?,
    currentLayout: Layout,
    user: User,
    giaFilterState: GiaRecordFilterCriteria,
    setupFilterState: SetupRecordFilterCriteria,
    filterEvent: (FilterEvent) -> Unit,
    recordsState: RecordState,
    toggleDialog: (DialogEvent) -> Unit,
    setLayout: () -> Unit,
    onRecordClick: (Pair<String, String>?) -> Unit,
    onFileClick: () -> Unit,
    selectYear: (Int?) -> Unit,
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
                currentLayout = currentLayout,
                giaFilterState = giaFilterState,
                setupFilterState = setupFilterState,
                toggleDialog = toggleDialog,
                setLayout = setLayout,
                resetFilter = { filterEvent(FilterEvent.ResetFilter) }
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
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val records = when(recordsState){
                    is RecordState.Error -> emptyList()
                    RecordState.Loading -> emptyList()
                    is RecordState.Success -> recordsState.records
                }
                YearDropdownMenu(
                    modifier = Modifier.weight(1f),
                    selectedYear = selectedYear,
                    onYearSelected = selectYear
                )
                Text(
                    "Records Found: ${records.size}",
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(Modifier.width(16.dp))
                LayoutSwitch(
                    firstText = "Table",
                    secondText = "Card",
                    isTableLayout = currentLayout == Layout.TABLE,
                    setLayout = setLayout,
                )
            }
            RecordsContainer(currentLayout = currentLayout, recordsState = recordsState, onRecordClick = onRecordClick)
        }
    }
}

@Composable
fun AppHeader(
    modifier: Modifier = Modifier,
    user: User,
    currentLayout: Layout,
    currentTab: Type,
    giaFilterState: GiaRecordFilterCriteria,
    setupFilterState: SetupRecordFilterCriteria,
    setLayout: () -> Unit,
    toggleDialog: (DialogEvent) -> Unit,
    resetFilter: () -> Unit
) {
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
                RarDrawer(
                    currentLayout = currentLayout,
                    setLayout = setLayout,
                    toggleDialog = toggleDialog
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
    recordsState: RecordState,
    onRecordClick: (Pair<String, String>?) -> Unit
) {
    val pagerState = rememberPagerState(
        pageCount = { 2 },
    )


    AnimatedContent(
        targetState = recordsState,
    ) { state ->
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            when (state) {
                is RecordState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(painter = painterResource(R.drawable.error_data), contentDescription = "error_data", Modifier.size(164.dp))
                        Text(state.error, maxLines = 2, overflow = TextOverflow.Clip, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineMedium)
                    }
                }
                RecordState.Loading -> RarLoadingProgressIndicator()
                is RecordState.Success -> {
                    if(state.records.isEmpty()) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Image(painter = painterResource(R.drawable.no_data), contentDescription = "no_data", Modifier.size(164.dp))
                            Text("Empty Records.", textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineMedium)
                        }
                    } else{
                        VerticalPager(
                            state = pagerState,
                            pageSize = object : PageSize {
                                override fun Density.calculateMainAxisPageSize(
                                    availableSpace: Int,
                                    pageSpacing: Int
                                ): Int {
                                    return (availableSpace - 2 * pageSpacing) / 2
                                }
                            },
                            pageNestedScrollConnection = if (pagerState.currentPage == 1) {
                                object : NestedScrollConnection {}
                            } else {
                                PagerDefaults.pageNestedScrollConnection(state = pagerState, orientation = Orientation.Vertical)
                            },
                            modifier = Modifier.fillMaxSize()
                        ) { page ->
                            when (page) {
                                0 -> {
                                    val title = when(state.records.firstOrNull()){
                                        is GiaRecord -> "Total Project Cost per Programs"
                                        is SetupRecord -> "Total Amount Approved per Sectors"
                                        else -> ""
                                    }
                                    Column(
                                        modifier = Modifier.align(Alignment.Center),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(title, textAlign = TextAlign.Center,
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                        RarPieChart(
                                            records = state.records
                                        )
                                    }
                                }
                                1 -> RecordLayout(
                                    currentLayout = currentLayout,
                                    records = state.records,
                                    modifier = Modifier.fillMaxSize(),
                                    onRecordClick = onRecordClick
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RecordLayout(
    currentLayout: Layout,
    records: List<Record>,
    modifier: Modifier = Modifier,
    onRecordClick: (Pair<String, String>?) -> Unit
) {
    AnimatedContent(modifier = modifier, targetState = currentLayout) { layout ->
        when(layout){
            Layout.CARD -> RecordCardLayout(records, onRecordClick = onRecordClick)
            Layout.TABLE -> RecordTableLayout(records, onRecordClick = onRecordClick)
        }
    }
}

@Composable
fun RarPieChart(modifier: Modifier = Modifier, records: List<Record>) {
    val firstRecord = records.firstOrNull()

    val names = when(firstRecord){
        is GiaRecord -> GiaClass.entries.map { it.value }
        is SetupRecord -> SectorType.entries.map { it.value }
        else -> emptyList()
    }

    val data = names.map { name ->
        when (firstRecord) {
            is GiaRecord -> {
                val total = (records as List<GiaRecord>)
                    .filter { name == GiaClass.from(it.className)?.value }
                    .sumOf { it.projectCost }

                PieChartData(
                    data = total,
                    partName = name,
                    color = giaClassColors[GiaClass.from(name)] ?: Color.Gray
                )
            }
            is SetupRecord -> {
                val total = (records as List<SetupRecord>)
                    .filter { name == SectorType.from(it.sector)?.value }
                    .sumOf { it.amountApproved ?: 0.0 }

                PieChartData(
                    data = total,
                    partName = name,
                    color = sectorTypeColors[SectorType.from(name)] ?: Color.Gray
                )
            }

            else -> PieChartData(data = 0.0, partName = "unknown", color = Color.White)
        }
    }

    PieChart(
        modifier = modifier.size(278.dp),
        pieChartData = data,
        ratioLineColor = Color.LightGray,
        legendPosition = LegendPosition.BOTTOM,
        textRatioStyle = TextStyle(color = Color.Gray),
    )
}

@Composable
fun RarRadarChart(modifier: Modifier = Modifier, records: List<Record>) {

    val adsf = ""
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