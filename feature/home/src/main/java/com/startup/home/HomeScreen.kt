package com.startup.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.startup.common.util.formatWithLocale
import com.startup.design_system.widget.actionbar.MainLogoActionBar
import com.startup.design_system.widget.speechbubble.SpeechBubble
import com.startup.home.egg.EggLayoutModel
import com.startup.home.egg.getEggLayoutModel
import com.startup.home.egg.model.EggKind
import com.startup.home.menu.HistoryItemModel
import com.startup.ui.WalkieTheme
import com.startup.ui.noRippleClickable
import withBold
import withUnderline

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigationEvent: (HomeScreenNavigationEvent) -> Unit
) {
    val state = viewModel.state as StepCounterState
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            MainLogoActionBar(isExistAlarm = false) {
                onNavigationEvent.invoke(HomeScreenNavigationEvent.MoveToNotification)
            }
        },
        containerColor = WalkieTheme.colors.white
    ) { paddingValues ->
        HomeContent(
            paddingValues = paddingValues,
            scrollState = scrollState,
            stepCount = state.steps,
            onNavigationEvent = onNavigationEvent
        )
    }
}

@Composable
private fun HomeContent(
    paddingValues: PaddingValues,
    scrollState: ScrollState,
    stepCount: Int,
    onNavigationEvent: (HomeScreenNavigationEvent) -> Unit
) {
    // 스크롤 필요 여부 계산
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.dp
    val topPadding = paddingValues.calculateTopPadding()
    val availableHeight = screenHeightDp - topPadding - 50.dp
    val minRequiredHeight = 431.dp + 18.dp + 180.dp
    val needsScroll = minRequiredHeight > availableHeight


    Column(
        modifier = Modifier
            .background(color = WalkieTheme.colors.white)
            .padding(
                top = paddingValues.calculateTopPadding(),
                start = 16.dp,
                end = 16.dp,
            )
            .then(if (needsScroll) Modifier.verticalScroll(scrollState) else Modifier)
            .padding(bottom = 50.dp)

    ) {
        Spacer(modifier = Modifier.height(8.dp))
        if (!needsScroll) {
            // 충분히 큰 화면: 남은 공간을 모두 차지
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                EggAndPartnerSection(
                    stepCount = stepCount,
                    onNavigationEvent = onNavigationEvent,
                    useFixedHeight = false
                )
            }
        } else {
            // 작은 화면: 고정 높이 사용 (스크롤 필요)
            EggAndPartnerSection(
                stepCount = stepCount,
                onNavigationEvent = onNavigationEvent,
                useFixedHeight = true
            )
        }

        Spacer(modifier = Modifier.height(18.dp))
        MyHistorySection(onNavigationEvent)
    }
}

@Composable
private fun EggAndPartnerSection(
    stepCount: Int,
    onNavigationEvent: (HomeScreenNavigationEvent) -> Unit,
    useFixedHeight: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (useFixedHeight) Modifier.height(431.dp)
                else Modifier.fillMaxHeight()
            )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (useFixedHeight) Modifier.height(371.dp)
                        else Modifier
                            .weight(1f)
                            .heightIn(min = 371.dp)
                    )
                    .clip(RoundedCornerShape(20.dp))
            ) {
                EggLayout(
                    modifier = Modifier.fillMaxSize(),
                    step = stepCount,
                    eggKind = EggKind.Rare,
                    onNavigationEvent = onNavigationEvent
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            PartnerInfoBox()
        }
        //todo 파트너 캐릭터 교체
        Image(
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.BottomEnd)
                .offset(x = (-8).dp),
            painter = painterResource(R.drawable.jelly_1),
            contentDescription = stringResource(R.string.desc_partner)
        )
    }
}

@Composable
private fun PartnerInfoBox() {
    Box(
        modifier = Modifier
            .height(52.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(WalkieTheme.colors.gray100),
        contentAlignment = Alignment.CenterStart,
    ) {
        val partner = "해파리"
        Text(
            text = stringResource(R.string.home_walking_with, partner).withBold(partner),
            modifier = Modifier.padding(start = 16.dp),
            textAlign = TextAlign.Start,
            style = WalkieTheme.typography.body2
        )
    }
}

@Composable
private fun MyHistorySection(onNavigationEvent: (HomeScreenNavigationEvent) -> Unit) {
    Text(
        text = stringResource(R.string.home_my_history),
        style = WalkieTheme.typography.head4,
        color = WalkieTheme.colors.gray700
    )
    Spacer(modifier = Modifier.height(8.dp))
    HistoryItems(onNavigationEvent)
}

@Composable
private fun HistoryItems(onNavigationEvent: (HomeScreenNavigationEvent) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        getDefaultHistoryMenu().take(3).forEach { item ->
            HistoryItem(
                item = item,
                modifier = Modifier.weight(1f),
            ) { myHistoryKind ->
                when (myHistoryKind) {
                    HistoryItemModel.MyHistoryKind.GainEgg -> {
                        onNavigationEvent.invoke(HomeScreenNavigationEvent.MoveToGainEgg)
                    }

                    HistoryItemModel.MyHistoryKind.SpotArchive -> {
                        onNavigationEvent.invoke(HomeScreenNavigationEvent.MoveToSpotArchive)
                    }

                    HistoryItemModel.MyHistoryKind.GainCharacter -> {
                        onNavigationEvent.invoke(HomeScreenNavigationEvent.MoveToGainCharacter)
                    }
                }
            }
        }
    }
}

fun getDefaultHistoryMenu(): List<HistoryItemModel> {
    return listOf(
        HistoryItemModel(
            myHistoryKind = HistoryItemModel.MyHistoryKind.GainEgg,
            thumbnailDrawable = R.drawable.img_gain_eggs,
            titleString = R.string.home_gain_eggs,
            unitString = R.string.quantity_string
        ),
        HistoryItemModel(
            myHistoryKind = HistoryItemModel.MyHistoryKind.GainCharacter,
            thumbnailDrawable = R.drawable.img_hatching_characters,
            titleString = R.string.home_hatching_characters,
            unitString = R.string.group_characters_string
        ),
        HistoryItemModel(
            myHistoryKind = HistoryItemModel.MyHistoryKind.SpotArchive,
            thumbnailDrawable = R.drawable.img_spot_history,
            titleString = R.string.home_spot_history,
            unitString = R.string.quantity_string
        )
    )
}

@Composable
private fun HistoryItem(
    item: HistoryItemModel,
    modifier: Modifier = Modifier,
    onClickItem: (HistoryItemModel.MyHistoryKind) -> Unit
) {
    Column(
        modifier = modifier
            .padding(horizontal = 4.dp)
            .noRippleClickable {
                onClickItem.invoke(item.myHistoryKind)
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 비율을 유지하면서 너비에 맞춰 이미지 표시
        val aspectRatio = 69f / 104f
        Image(
            painter = painterResource(item.thumbnailDrawable),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f / aspectRatio)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.height(20.dp),
            text = stringResource(item.titleString),
            style = WalkieTheme.typography.head6,
            color = WalkieTheme.colors.gray700
        )
        Text(
            modifier = Modifier.height(20.dp),
            text = stringResource(item.unitString, 3),
            style = WalkieTheme.typography.body2,
            color = WalkieTheme.colors.gray500
        )
    }
}

@Composable
fun EggLayout(
    modifier: Modifier = Modifier,
    step: Int,
    eggKind: EggKind = EggKind.Empty,
    onNavigationEvent: (HomeScreenNavigationEvent) -> Unit,
) {
    val eggAttribute = getEggLayoutModel(eggKind)

    Box(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.3872f to eggAttribute.startGradient,
                        1f to eggAttribute.endGradient
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        EggContent(
            step = step,
            eggKind = eggKind,
            eggAttribute = eggAttribute,
            onNavigationEvent = onNavigationEvent
        )
    }
}

@Composable
private fun EggContent(
    step: Int,
    eggKind: EggKind,
    eggAttribute: EggLayoutModel,
    onNavigationEvent: (HomeScreenNavigationEvent) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        StepInformation(step = step)

        // 화면 너비에 맞게 크기 조정
        val maxEggSize = 360.dp
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp
        val eggSize = minOf(screenWidth - 12.dp, maxEggSize) // 좌우 패딩 6dp씩 고려

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 78.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            if (eggKind != EggKind.Empty) {
                SpeechBubble(
                    steps = step.formatWithLocale(),
                    modifier = Modifier
                        .offset(y = 30.dp)
                        .zIndex(5f),
                )
            }

            // 알 이미지
            Box {
                Image(
                    modifier = Modifier.size(eggSize),
                    painter = painterResource(eggAttribute.eggDrawable),
                    colorFilter = if (eggKind == EggKind.Empty) {
                        ColorFilter.tint(
                            WalkieTheme.colors.blue300,
                            blendMode = BlendMode.SrcIn
                        )
                    } else null,
                    contentDescription = stringResource(R.string.desc_empty_egg),
                )

                // 알 선택 텍스트
                if (eggKind == EggKind.Empty) {
                    Text(
                        text = stringResource(R.string.home_choice_egg).withUnderline(),
                        style = WalkieTheme.typography.head5,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = (-151).dp)
                            .noRippleClickable {
                                onNavigationEvent.invoke(HomeScreenNavigationEvent.MoveToGainEgg)
                            },
                        color = WalkieTheme.colors.blue50
                    )
                }
            }
        }

        // 이펙트 이미지
        if (eggKind != EggKind.Empty) {
            eggAttribute.effectDrawable?.let { drawableRes ->
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 78.dp)
                        .align(Alignment.BottomCenter),
                    painter = painterResource(drawableRes),
                    contentDescription = stringResource(R.string.desc_empty_egg),
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }
}

@Composable
private fun StepInformation(step: Int) {
    Column(
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .wrapContentHeight()
                .padding(top = 20.dp, start = 16.dp)
        ) {
            Text(
                text = step.formatWithLocale(),
                style = WalkieTheme.typography.head1,
                color = Color.White
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = stringResource(R.string.home_step),
                style = WalkieTheme.typography.body1,
                color = Color.White,
                modifier = Modifier.padding(bottom = 5.dp)
            )
        }
        Spacer(Modifier.height(3.dp))
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .wrapContentHeight()
                .padding(start = 20.dp)
        ) {
            Text(
                text = stringResource(R.string.home_kilometer, step * 0.02),
                style = WalkieTheme.typography.head2,
                color = Color.White
            )
            Spacer(Modifier.width(5.dp))
            Text(
                text = stringResource(R.string.home_movement),
                style = WalkieTheme.typography.body2,
                color = Color.White,
            )
        }
    }
}