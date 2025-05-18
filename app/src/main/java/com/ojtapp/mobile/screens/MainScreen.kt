package com.ojtapp.mobile.screens

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
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
import com.ojtapp.mobile.components.RarLoadingProgressIndicator
import com.ojtapp.mobile.components.RecordCardLayout
import com.ojtapp.mobile.components.RecordTableLayout
import com.ojtapp.mobile.components.RightNavigationDrawer
import com.ojtapp.mobile.components.SelectedRecordDialog
import com.ojtapp.mobile.components.TypeTabRow
import com.ojtapp.mobile.components.WarningDialog
import com.ojtapp.mobile.components.YearDropdownMenu
import com.ojtapp.mobile.components.util.GiaRecordFilterCriteria
import com.ojtapp.mobile.components.util.SetupRecordFilterCriteria
import com.ojtapp.mobile.components.util.countFilters
import com.ojtapp.mobile.components.util.formatAsPeso
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
import kotlinx.coroutines.launch

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

    var isDrawerOpen by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            AppHeader(
                user = user,
                currentTab = currentTab,
                giaFilterState = giaFilterState,
                setupFilterState = setupFilterState,
                toggleRightDrawer = { isDrawerOpen = !isDrawerOpen },
                resetFilter = { filterEvent(FilterEvent.ResetFilter) }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = !isDrawerOpen,
                enter = slideInVertically(
                    spring(
                        stiffness = Spring.StiffnessMedium,
                        visibilityThreshold = IntOffset.VisibilityThreshold
                    ),
                    initialOffsetY = { it }
                ),
                exit = slideOutVertically(
                    spring(
                        stiffness = Spring.StiffnessMedium,
                        visibilityThreshold = IntOffset.VisibilityThreshold
                    ),
                    targetOffsetY = { it * 2 }
                ) + fadeOut(),
            ) {
                TypeTabRow(
                    filterAmount = countFilters(currentTab, giaFilterState, setupFilterState),
                    selectedTabIndex = currentTab.ordinal,
                    onFilterSelect = { showBottomSheet = true },
                    onSelectedTab = onSelectedTab,
                    onFileClick = onFileClick
                )
            }
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
            val records = when(recordsState){
                is RecordState.Error -> emptyList()
                RecordState.Loading -> emptyList()
                is RecordState.Success -> recordsState.records
            }
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .shadowWithClipIntersect(elevation = 4.dp, shape = RectangleShape)
                    .background(MaterialTheme.colorScheme.surfaceBright)
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .padding(bottom = 16.dp)
                    ,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    YearDropdownMenu(
                        modifier = Modifier.weight(1f),
                        selectedYear = selectedYear,
                        onYearSelected = selectYear
                    )
                    Spacer(Modifier.width(16.dp))
                    Text(
                        "Records Found: ${records.size}",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            RecordsContainer(
                currentLayout = currentLayout,
                isDrawerOpen = isDrawerOpen,
                onCloseDrawer = { isDrawerOpen = false },
                recordsState = recordsState,
                toggleDialog = toggleDialog,
                setLayout = setLayout,
                onRecordClick = onRecordClick
            )
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
    toggleRightDrawer: () -> Unit,
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
                Spacer(Modifier.weight(1f))
                IconButton(
                    onClick = toggleRightDrawer
                ) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = "rar_menu")
                }
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
    isDrawerOpen: Boolean,
    onCloseDrawer: () -> Unit,
    setLayout: () -> Unit,
    toggleDialog: (DialogEvent) -> Unit,
    onRecordClick: (Pair<String, String>?) -> Unit
) {
    val pagerState = rememberPagerState(
        pageCount = { 2 },
    )
    val scope = rememberCoroutineScope()

    RightNavigationDrawer(
        drawerContent = {
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LayoutSwitch(
                    isTableLayout = currentLayout == Layout.TABLE,
                    setLayout = setLayout
                )
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .background(if(pagerState.currentPage == 0) MaterialTheme.colorScheme.onSurface.copy(.1f) else Color.Unspecified)
                    .clickable {
                       scope.launch {
                            pagerState.animateScrollToPage(
                                0,
                                animationSpec = tween()
                            )
                        }.invokeOnCompletion {
                            onCloseDrawer()
                        }
                    },
                    contentAlignment = Alignment.Center
                ){
                    Text("1", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(8.dp))
                }
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .background(if(pagerState.currentPage == 1) MaterialTheme.colorScheme.onSurface.copy(.1f) else Color.Unspecified)
                    .clickable {
                        scope.launch {
                            pagerState.animateScrollToPage(
                                1,
                                animationSpec = tween()
                            )
                        }.invokeOnCompletion {
                            onCloseDrawer()
                        }
                    },
                    contentAlignment = Alignment.Center
                ){
                    Text("2", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(8.dp))
                }
                Box(
                    Modifier
                        .weight(1f)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = {
                                toggleDialog(DialogEvent.OpenDialog)
                            }),
                    contentAlignment = Alignment.BottomCenter
                ){
                    Text(
                        text = "leave",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(Modifier.height(12.dp))
            }
        },
        isDrawerOpen = isDrawerOpen,
        onCloseDrawer = onCloseDrawer
    ) {
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
                                pageSpacing = 8.dp,
                                flingBehavior = PagerDefaults.flingBehavior(
                                    state = pagerState,
                                    snapPositionalThreshold = 0.15f,
                                ),
                                snapPosition = SnapPosition.End,
                                modifier = Modifier.fillMaxSize()
                            ) { page ->
                                when(page){
                                    0 -> {
                                        AnalyticsPage(
                                            records = state.records,
                                            onRecordClick = onRecordClick
                                        )
                                    }
                                    1 -> {
                                        RecordLayout(
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

@OptIn(ExperimentalLayoutApi::class)
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

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(8.dp))
        FlowRow(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            data.forEach {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(12.dp).clip(RoundedCornerShape(15.dp)).background(it.color))
                    Spacer(Modifier.width(8.dp))
                    Text(it.partName, color = MaterialTheme.colorScheme.outline, style = MaterialTheme.typography.labelMedium)
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        PieChart(
            modifier = modifier
                .height(234.dp)
                .fillMaxWidth(),
            pieChartData = data.filter { it.data != 0.0 },
            ratioLineColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = .85f),
            outerCircularColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = .85f),
            legendPosition = LegendPosition.DISAPPEAR,
            textRatioStyle = TextStyle(color = Color.Gray),
        )
    }
}

@Composable
fun AnalyticsPage(
    records: List<Record>,
    onRecordClick: (Pair<String, String>) -> Unit
) {

    val firstRecord = records.firstOrNull()

    val pieChartTitle = when(firstRecord){
        is GiaRecord -> "Percentage of the Total Project Cost per Programs"
        is SetupRecord -> "Percentage of the Total Amount Approved per Sectors"
        else -> ""
    }

    val firstDataTitle = when(firstRecord){
        is GiaRecord -> "Largest Project Cost"
        is SetupRecord -> "Largest Amount Approved"
        else -> ""
    }

    val secondDataTitle = when(firstRecord){
        is GiaRecord -> "Program with Total Largest Project Cost"
        is SetupRecord -> "Sector with the Total Largest Amount Approved"
        else -> ""
    }

    val (largestAmountProject, largestAmountValue, largestFileLocation) = when (firstRecord) {
        is GiaRecord -> {
            val largest = (records as List<GiaRecord>).maxByOrNull { it.projectCost }
            val title = (largest?.projectTitle + ": ${largest?.className}")
            val amount = largest?.projectCost ?: 0.0
            val file = largest?.fileLocation ?: ""
            Triple(title, amount, file)
        }
        is SetupRecord -> {
            val largest = (records as List<SetupRecord>).maxByOrNull { it.amountApproved ?: 0.0 }
            val firm = (largest?.firmName + ": ${largest?.sector}")
            val amount = largest?.amountApproved ?: 0.0
            val file = largest?.fileLocation ?: ""
            Triple(firm, amount, file)
        }
        else -> Triple("", 0.0, "")
    }

    val (secondDataText, secondDataAmount) = when (firstRecord) {
        is GiaRecord -> {
            val grouped = (records as List<GiaRecord>)
                .groupBy { GiaClass.from(it.className)?.value ?: "Unknown Program" }

            val (topProgram, totalAmount) = grouped.maxByOrNull { entry ->
                entry.value.sumOf { it.projectCost }
            }?.let { it.key to it.value.sumOf { record -> record.projectCost } } ?: ("Unknown Program" to 0.0)

            topProgram to totalAmount
        }

        is SetupRecord -> {
            val grouped = (records as List<SetupRecord>)
                .groupBy { SectorType.from(it.sector)?.value ?: "Unknown Sector" }

            val (topSector, totalAmount) = grouped.maxByOrNull { entry ->
                entry.value.sumOf { it.amountApproved ?: 0.0 }
            }?.let { it.key to it.value.sumOf { record -> record.amountApproved ?: 0.0 } } ?: ("Unknown Sector" to 0.0)

            topSector to totalAmount
        }

        else -> "" to 0.0
    }

    val totalAmountTitle = when (firstRecord) {
        is GiaRecord -> "Total Project Cost of All Programs"
        is SetupRecord -> "Total Amount Approved of All Sectors"
        else -> ""
    }

    val totalAmountValue = when (firstRecord) {
        is GiaRecord -> (records as List<GiaRecord>).sumOf { it.projectCost }
        is SetupRecord -> (records as List<SetupRecord>).sumOf { it.amountApproved ?: 0.0 }
        else -> 0.0
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = firstDataTitle,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = largestAmountProject,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                text = largestAmountValue.formatAsPeso(),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall,
            )
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onRecordClick(
                            Pair(
                                largestAmountProject.substringBefore(":"),
                                largestFileLocation
                            )
                        )
                    }
            ) {
                Text(
                    text = "Open Directory",
                    color = ButtonDefaults.textButtonColors().contentColor
                )
            }
        }

        Spacer(Modifier.height(4.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = secondDataTitle,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = secondDataText,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                text = secondDataAmount.formatAsPeso(),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall,
            )
        }

        Spacer(Modifier.height(12.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = totalAmountTitle,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = totalAmountValue.formatAsPeso(),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
            )
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = pieChartTitle,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        RarPieChart(records = records)
        Spacer(Modifier.height(42.dp))
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