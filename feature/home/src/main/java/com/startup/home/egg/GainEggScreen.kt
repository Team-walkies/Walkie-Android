package com.startup.home.egg

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.startup.common.util.DateUtil
import com.startup.common.util.EMPTY_STRING
import com.startup.design_system.widget.actionbar.PageActionBar
import com.startup.design_system.widget.actionbar.PageActionBarType
import com.startup.design_system.widget.progress.ProgressMedium
import com.startup.design_system.widget.tag.TagMedium
import com.startup.home.R
import com.startup.home.egg.model.EggKind
import com.startup.home.egg.model.GainEggViewState
import com.startup.home.egg.model.GainEggViewStateImpl
import com.startup.home.egg.model.MyEggModel
import com.startup.ui.WalkieTheme
import com.startup.ui.noRippleClickable
import kotlinx.coroutines.flow.MutableStateFlow
import java.text.NumberFormat
import java.util.Locale

/** 메인 뷰 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GainEggScreen(
    viewState: GainEggViewState,
    onNavigationEvent: (GainEggScreenNavigationEvent) -> Unit
) {
    val eggList by viewState.eggList.collectAsStateWithLifecycle()
    var selectedEgg: MyEggModel? by remember {
        mutableStateOf(null)
    }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val localConfiguration = LocalConfiguration.current
    val screenHeightDp = localConfiguration.screenHeightDp.toFloat()
    val pagerHeight = screenHeightDp.dp
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WalkieTheme.colors.white)
    ) {
        PageActionBar(
            PageActionBarType.TitleAndRightActionBarType(
                onBackClicked = {
                    onNavigationEvent.invoke(GainEggScreenNavigationEvent.Back)
                },
                title = EMPTY_STRING,
                rightActionTitle = stringResource(R.string.right_title_egg_info),
                rightActionClicked = {
                    onNavigationEvent.invoke(GainEggScreenNavigationEvent.MoveToEggGainProbabilityScreen)
                }
            )
        )
        LazyColumn(
            modifier = Modifier
                .padding(top = 12.dp, start = 16.dp, end = 16.dp)
        ) {
            item {
                Row {
                    Text(
                        stringResource(R.string.current_gain_egg),
                        style = WalkieTheme.typography.head2.copy(color = WalkieTheme.colors.gray700)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        eggList.size.toString(),
                        style = WalkieTheme.typography.head2.copy(color = WalkieTheme.colors.gray500)
                    )
                }
            }
            if (eggList.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        stringResource(R.string.current_gain_egg_choice),
                        style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray500)
                    )
                }
                item {
                    LazyVerticalGrid(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .height(pagerHeight),
                        columns = GridCells.Fixed(MAX_COLUMN_COUNT),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        itemsIndexed(
                            items = eggList,
                            key = { _, item -> item.eggId },
                        ) { _, item ->
                            EggGridItemComponent(egg = item) {
                                selectedEgg = it
                            }
                        }
                    }
                }
            } else {
                item {
                    EmptyGainEggView()
                }
            }
        }
    }
    if (selectedEgg != null) {
        EggBottomModal(
            onClickCancel = {
                selectedEgg = null
            },
            egg = selectedEgg!!,
            sheetState = sheetState
        )
    }
}

/** 보유한 알이 없을 때 보여질 컨텐츠 */
@Composable
private fun EmptyGainEggView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 138.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.empty_gain_egg),
            style = WalkieTheme.typography.body1.copy(color = WalkieTheme.colors.gray400),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = painterResource(R.drawable.egg_empty),
            stringResource(R.string.desc_egg_empty)
        )
    }
}

/** 바텀 시트, Preview 를 위해 뷰 파일과 분리 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EggBottomModal(
    onClickCancel: () -> Unit,
    egg: MyEggModel,
    sheetState: SheetState
) {
    WalkieTheme {
        ModalBottomSheet(
            onDismissRequest = { onClickCancel() },
            sheetState = sheetState,
            tonalElevation = 24.dp,
            containerColor = WalkieTheme.colors.white,
            contentColor = WalkieTheme.colors.white,
            scrimColor = WalkieTheme.colors.blackOpacity60,
        ) {
            EggBottomModalContent(egg)
        }
    }
}

/** 알 detail 및 같이 걸을 알 선택할 수 있는 바텀 시트 View */
@Composable
private fun EggBottomModalContent(egg: MyEggModel) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .background(color = WalkieTheme.colors.white)
            .padding(bottom = 28.dp, top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(egg.eggKind.drawableResId),
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(R.string.desc_egg),
            modifier = Modifier
                .size(180.dp),
        )
        Spacer(modifier = Modifier.height(4.dp))
        TagMedium(
            text = stringResource(egg.eggKind.rankStrResId),
            textColor = egg.eggKind.getTextColor()
        )
        val formatter = NumberFormat.getNumberInstance(Locale.getDefault())

        Spacer(modifier = Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = kotlin.runCatching { formatter.format(egg.nowStep) }
                    .getOrElse { egg.nowStep.toString() },
                style = WalkieTheme.typography.body1.copy(color = WalkieTheme.colors.gray700)
            )
            Text(
                text = "/",
                style = WalkieTheme.typography.body1.copy(color = WalkieTheme.colors.gray700)
            )
            Text(
                text = kotlin.runCatching { formatter.format(egg.needStep) }
                    .getOrElse { egg.needStep.toString() },
                style = WalkieTheme.typography.body1.copy(color = WalkieTheme.colors.gray500)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        ProgressMedium(
            modifier = Modifier.padding(horizontal = 90.dp),
            egg.nowStep.toFloat() / egg.needStep.toFloat()
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1F)
                    .background(WalkieTheme.colors.gray50, shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.obtained_date_label),
                    style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray500)
                )
                Text(
                    text = egg.obtainedDate,
                    style = WalkieTheme.typography.head6.copy(color = WalkieTheme.colors.gray700)
                )
            }
            Column(
                modifier = Modifier
                    .weight(1F)
                    .background(WalkieTheme.colors.gray50, shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.obtained_position_label),
                    style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray500)
                )
                Text(
                    text = egg.obtainedPosition,
                    style = WalkieTheme.typography.head6.copy(color = WalkieTheme.colors.gray700)
                )
            }
        }
        val label = if (egg.play) {
            stringResource(R.string.playing_this_egg)
        } else {
            stringResource(R.string.play_this_egg)
        }
        val backgroundColor = if (egg.play) {
            WalkieTheme.colors.gray200
        } else {
            WalkieTheme.colors.blue300
        }
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier.noRippleClickable {
                if (!egg.play) {
                    // TODO 같이 걷는 알 변경
                }
            }
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(backgroundColor, shape = RoundedCornerShape(12.dp))
                    .padding(vertical = 15.dp),
                text = label,
                textAlign = TextAlign.Center,
                style = WalkieTheme.typography.body1.copy(color = WalkieTheme.colors.white)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewBottomModal() {
    WalkieTheme {
        EggBottomModalContent(
            MyEggModel(
                eggId = 2,
                nowStep = 8334,
                needStep = 10000,
                play = true,
                characterId = -1,
                eggKind = EggKind.Epic,
                obtainedPosition = "대전시 유성구",
                obtainedDate = DateUtil.convertDateTimeFormat("2024-01-12 12:20:10"),
            )
        )
    }
}

@PreviewScreenSizes
@Composable
private fun PreviewGainEggScreen() {
    WalkieTheme {
        GainEggScreen(GainEggViewStateImpl(eggList = MutableStateFlow(emptyList()))) {}
    }
}


private const val MAX_COLUMN_COUNT = 2
