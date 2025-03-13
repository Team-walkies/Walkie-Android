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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.startup.common.util.formatWithLocale
import com.startup.design_system.widget.actionbar.MainLogoActionBar
import com.startup.design_system.widget.speechbubble.SpeechBubble
import com.startup.home.egg.EggKind
import com.startup.home.egg.EggLayoutModel
import com.startup.home.egg.getEggLayoutModel
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
    Column(
        modifier = Modifier
            .background(color = WalkieTheme.colors.white)
            .padding(
                top = paddingValues.calculateTopPadding(),
                start = 16.dp,
                end = 16.dp,
                bottom = 8.dp
            )
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        EggAndPartnerSection(stepCount = stepCount, onNavigationEvent = onNavigationEvent)
        Spacer(modifier = Modifier.height(18.dp))
        MyHistorySection(onNavigationEvent)
    }
}

@Composable
private fun EggAndPartnerSection(
    stepCount: Int,
    onNavigationEvent: (HomeScreenNavigationEvent) -> Unit
) {
    Box {
        Column {
            EggLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(371.dp)
                    .clip(RoundedCornerShape(20.dp)),
                step = stepCount,
                eggKind = EggKind.Empty,
                onNavigationEvent
            )
            Spacer(modifier = Modifier.height(8.dp))
            PartnerInfoBox()
        }
        //todo 파트너 캐릭터별 교체 필요
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
        val text = stringResource(R.string.home_walking_with, partner) // "%s와 함께 걷는중.."
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

        if (eggKind != EggKind.Empty) {
            eggAttribute.effectDrawable?.let { drawableRes ->
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 85.dp)
                        .align(Alignment.BottomCenter),
                    painter = painterResource(drawableRes),
                    contentDescription = stringResource(R.string.desc_empty_egg),
                    contentScale = ContentScale.FillWidth
                )
            }

            SpeechBubble(
                steps = step.formatWithLocale(),
                modifier = Modifier
                    .offset(y = (-193).dp)
                    .align(Alignment.BottomCenter)
                    .zIndex(5f),
            )
        }

        Image(
            modifier = Modifier
                .size(296.dp)
                .offset(y = 85.dp)
                .align(Alignment.BottomCenter),
            painter = painterResource(eggAttribute.eggDrawable),
            colorFilter = if (eggKind == EggKind.Empty) {
                ColorFilter.tint(
                    WalkieTheme.colors.blue300,
                    blendMode = BlendMode.SrcIn
                )
            } else null,
            contentDescription = stringResource(R.string.desc_empty_egg),
        )

        if (eggKind == EggKind.Empty) {
            Text(
                text = stringResource(R.string.home_choice_egg).withUnderline(),
                style = WalkieTheme.typography.head5,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = (-66).dp)
                    .noRippleClickable {
                        onNavigationEvent.invoke(HomeScreenNavigationEvent.MoveToGainEgg)
                    },
                color = WalkieTheme.colors.blue50
            )
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