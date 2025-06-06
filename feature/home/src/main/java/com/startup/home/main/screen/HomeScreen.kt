package com.startup.home.main.screen

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.startup.common.base.BaseUiState
import com.startup.common.extension.formatWithLocale
import com.startup.common.extension.moveToAppDetailSetting
import com.startup.common.extension.shimmerEffect
import com.startup.common.extension.shimmerEffectGray200
import com.startup.common.extension.withBold
import com.startup.common.extension.withUnderline
import com.startup.design_system.ui.WalkieTheme
import com.startup.design_system.ui.noRippleClickable
import com.startup.design_system.widget.actionbar.MainLogoActionBar
import com.startup.design_system.widget.modal.PrimaryTwoButtonModal
import com.startup.design_system.widget.permission.PermissionInduction
import com.startup.design_system.widget.speechbubble.SpeechBubble
import com.startup.ga.EventNameConst
import com.startup.ga.LocalAnalyticsHelper
import com.startup.ga.logEvent
import com.startup.home.HomeScreenNavigationEvent
import com.startup.home.R
import com.startup.home.main.HomeViewState
import com.startup.model.character.WalkieCharacter
import com.startup.model.egg.EggKind
import com.startup.model.egg.EggLayoutModel
import com.startup.model.egg.MyEggModel
import com.startup.model.egg.getEggLayoutModel
import com.startup.model.home.HistoryItemModel


@Composable
fun HomeScreen(
    state: HomeViewState,
    onNavigationEvent: (HomeScreenNavigationEvent) -> Unit
) {
    val scrollState = rememberScrollState()


    Scaffold(
        topBar = {
            MainLogoActionBar(isExistAlarm = false, isShowAlarmIcon = false) {
                onNavigationEvent.invoke(HomeScreenNavigationEvent.MoveToNotification)
            }
        },
        containerColor = WalkieTheme.colors.white
    ) { paddingValues ->
        HomeContent(
            paddingValues = paddingValues,
            scrollState = scrollState,
            viewState = state,
            onNavigationEvent = onNavigationEvent
        )
    }
}


@Composable
private fun HomeContent(
    viewState: HomeViewState,
    paddingValues: PaddingValues,
    scrollState: ScrollState,
    onNavigationEvent: (HomeScreenNavigationEvent) -> Unit
) {
    // 스크롤 필요 여부 계산
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.dp
    val availableHeight = screenHeightDp - 93.dp  // 상하단 패딩 50dp + 43dp 제외
    val halfAvailableHeight = availableHeight / 2  // 나머지 영역의 절반
    val needsScroll = halfAvailableHeight < 360.dp  // 이 값이 360dp보다 작으면 스크롤 필요


    val stepCountState by viewState.stepsUiState.collectAsStateWithLifecycle()
    val myEggModelState by viewState.currentWalkEggUiState.collectAsStateWithLifecycle()
    val walkieCharacterState by viewState.currentWalkCharacterUiState.collectAsStateWithLifecycle()

    val showActivityRecognitionPermissionAlert by viewState.showActivityRecognitionPermissionAlert.collectAsStateWithLifecycle()
    val showBackgroundLocationPermissionAlert by viewState.showBackgroundLocationPermissionAlert.collectAsStateWithLifecycle()

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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                EggAndPartnerSection(
                    stepCountState = stepCountState,
                    eggModelState = myEggModelState,
                    walkieCharacterState = walkieCharacterState,
                    showActivityRecognitionPermissionAlert = showActivityRecognitionPermissionAlert,
                    showBackgroundLocationPermissionAlert = showBackgroundLocationPermissionAlert,
                    onNavigationEvent = onNavigationEvent,
                    useFixedHeight = false
                )
            }
        } else {
            EggAndPartnerSection(
                stepCountState = stepCountState,
                eggModelState = myEggModelState,
                walkieCharacterState = walkieCharacterState,
                showActivityRecognitionPermissionAlert = showActivityRecognitionPermissionAlert,
                showBackgroundLocationPermissionAlert = showBackgroundLocationPermissionAlert,
                onNavigationEvent = onNavigationEvent,
                useFixedHeight = true
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        MyHistorySection(
            viewState = viewState,
            onNavigationEvent = onNavigationEvent
        )
    }
}

@Composable
private fun EggAndPartnerSection(
    stepCountState: BaseUiState<Pair<Int, Int>>,
    eggModelState: BaseUiState<MyEggModel>,
    walkieCharacterState: BaseUiState<WalkieCharacter>,
    showActivityRecognitionPermissionAlert: Boolean,
    showBackgroundLocationPermissionAlert: Boolean,
    onNavigationEvent: (HomeScreenNavigationEvent) -> Unit,
    useFixedHeight: Boolean
) {

    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.dp
    val eggAreaHeight = (screenHeightDp * 0.5f).coerceAtLeast(360.dp)
    val analyticsHelper = LocalAnalyticsHelper.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (useFixedHeight) Modifier.height(eggAreaHeight + 60.dp) // ~~ 함께 걷는중 영역도 고려
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
                        if (useFixedHeight)
                            Modifier.height(maxOf(eggAreaHeight, 360.dp))
                        else
                            Modifier
                                .weight(1f)
                                .heightIn(min = 360.dp)
                    )

                    .clip(RoundedCornerShape(20.dp))
            ) {
                EggLayout(
                    modifier = Modifier.fillMaxSize(),
                    stepCountState = stepCountState,
                    eggModelState = eggModelState,
                    showActivityRecognitionPermissionAlert = showActivityRecognitionPermissionAlert,
                    showBackgroundLocationPermissionAlert = showBackgroundLocationPermissionAlert,
                    onNavigationEvent = onNavigationEvent,
                    eggAreaHeight = eggAreaHeight.coerceAtLeast(360.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (walkieCharacterState.isShowShimmer) {
                Box(
                    modifier = Modifier
                        .height(52.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(WalkieTheme.colors.gray100)
                        .shimmerEffect()
                )
            } else {
                PartnerInfoBox(stringResource(walkieCharacterState.data.characterNameResId))
            }
        }
        if (!walkieCharacterState.isShowShimmer) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-8).dp),
            ) {

                PermissionSection(
                    showPermission = showActivityRecognitionPermissionAlert,
                    permissionTitle = R.string.permission_activity_recognition_alert,
                    dialogTitleResId = R.string.permission_activity_recognition_title,
                    dialogMessageResId = R.string.permission_activity_recognition_message,
                    textAlign = TextAlign.Center,
                    onPositiveClick = { context.moveToAppDetailSetting() },
                    modifier = Modifier.padding(top = 4.dp, start = 16.dp, end = 8.dp)
                )

                PermissionSection(
                    showPermission = showBackgroundLocationPermissionAlert,
                    permissionTitle = R.string.permission_background_location_alert,
                    dialogTitleResId = R.string.permission_location_dialog_title,
                    dialogMessageResId = R.string.permission_location_dialog_message,
                    textAlign = TextAlign.Start,
                    onPositiveClick = { context.moveToAppDetailSetting() },
                    modifier = Modifier.padding(top = 4.dp, start = 16.dp, end = 8.dp)
                )

                Row(
                    modifier = Modifier.align(alignment = Alignment.End),
                ) {
                    if (showActivityRecognitionPermissionAlert || showBackgroundLocationPermissionAlert) {
                        Image(
                            modifier = Modifier
                                .width(35.dp)
                                .height(21.dp),
                            painter = painterResource(R.drawable.permission_speech_bubble_tail),
                            contentDescription = null
                        )
                    }
                    Image(
                        modifier = Modifier
                            .size(120.dp)
                            .noRippleClickable {
                                analyticsHelper.logEvent(EventNameConst.MAIN_CHARACTER)
                            },
                        painter = painterResource(walkieCharacterState.data.characterImageResId),
                        contentDescription = stringResource(R.string.desc_partner)
                    )
                }

            }
        }
    }
}

@Composable
fun PermissionSection(
    showPermission: Boolean,
    permissionTitle: Int,
    dialogTitleResId: Int,
    dialogMessageResId: Int,
    textAlign: TextAlign,
    onPositiveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (showPermission) {
        var showDialog by remember { mutableStateOf(false) }

        PermissionInduction(
            title = permissionTitle,
            modifier = modifier
        ) {
            showDialog = true
        }

        if (showDialog) {
            PrimaryTwoButtonModal(
                title = stringResource(dialogTitleResId),
                subTitle = stringResource(dialogMessageResId),
                negativeText = stringResource(R.string.permission_dialog_negative),
                positiveText = stringResource(R.string.permission_dialog_positive),
                onClickNegative = {
                    showDialog = false
                },
                onClickPositive = {
                    onPositiveClick()
                    showDialog = false
                },
                textAlign = textAlign
            )
        }
    }
}

@Composable
private fun PartnerInfoBox(partnerName: String) {
    val analyticsHelper = LocalAnalyticsHelper.current
    Box(
        modifier = Modifier
            .height(52.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(WalkieTheme.colors.gray100)
            .noRippleClickable {
                analyticsHelper.logEvent(EventNameConst.MAIN_TOGETHER)
            },
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = stringResource(R.string.home_walking_with, partnerName).withBold(partnerName),
            modifier = Modifier.padding(start = 16.dp),
            textAlign = TextAlign.Start,
            style = WalkieTheme.typography.body2
        )
    }
}

@Composable
private fun MyHistorySection(
    viewState: HomeViewState,
    onNavigationEvent: (HomeScreenNavigationEvent) -> Unit
) {
    val eggCountState by viewState.currentGainEggCountUiState.collectAsStateWithLifecycle()
    val characterCountState by viewState.currentHatchedCharacterCountUiState.collectAsStateWithLifecycle()
    val currentRecordedSpotCountUiState by viewState.currentRecordedSpotCountUiState.collectAsStateWithLifecycle()

    if (eggCountState.isShowShimmer) {
        SkeletonHistoryTitleText()
    } else {
        Text(
            text = stringResource(R.string.home_my_history),
            style = WalkieTheme.typography.head4,
            color = WalkieTheme.colors.gray700
        )
    }

    Spacer(modifier = Modifier.height(8.dp))

    HistoryItems(
        eggCountState = eggCountState,
        characterCountState = characterCountState,
        spotCountState = if (currentRecordedSpotCountUiState.isShowShimmer) {
            BaseUiState(true, 0)
        } else {
            BaseUiState(false, currentRecordedSpotCountUiState.data)
        },
        onNavigationEvent = onNavigationEvent
    )
}


@Composable
private fun HistoryItems(
    eggCountState: BaseUiState<Int>,
    characterCountState: BaseUiState<Int>,
    spotCountState: BaseUiState<Int>,
    onNavigationEvent: (HomeScreenNavigationEvent) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val historyItems =
            if (eggCountState.isShowShimmer || characterCountState.isShowShimmer || spotCountState.isShowShimmer) {
                listOf(
                    BaseUiState(
                        true, HistoryItemModel(
                            myHistoryKind = HistoryItemModel.MyHistoryKind.GainEgg,
                            thumbnailDrawable = R.drawable.img_gain_eggs,
                            titleString = R.string.home_gain_eggs,
                            unitString = R.string.quantity_string,
                            count = 0
                        )
                    ),
                    BaseUiState(
                        true, HistoryItemModel(
                            myHistoryKind = HistoryItemModel.MyHistoryKind.GainCharacter,
                            thumbnailDrawable = R.drawable.img_hatching_characters,
                            titleString = R.string.home_hatching_characters,
                            unitString = R.string.group_characters_string,
                            count = 0
                        )
                    ),
                    BaseUiState(
                        true, HistoryItemModel(
                            myHistoryKind = HistoryItemModel.MyHistoryKind.SpotArchive,
                            thumbnailDrawable = R.drawable.img_spot_history,
                            titleString = R.string.home_spot_history,
                            unitString = R.string.quantity_string,
                            count = 0
                        )
                    )
                )
            } else {
                getDefaultHistoryMenu(
                    eggCount = eggCountState.data,
                    characterCount = characterCountState.data,
                    spotCount = spotCountState.data
                ).map { BaseUiState(false, it) }
            }

        historyItems.take(3).forEach { itemState ->
            HistoryItem(
                itemState = itemState,
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

fun getDefaultHistoryMenu(
    eggCount: Int,
    characterCount: Int,
    spotCount: Int
): List<HistoryItemModel> {
    return listOf(
        HistoryItemModel(
            myHistoryKind = HistoryItemModel.MyHistoryKind.GainEgg,
            thumbnailDrawable = R.drawable.img_gain_eggs,
            titleString = R.string.home_gain_eggs,
            unitString = R.string.quantity_string,
            count = eggCount
        ),
        HistoryItemModel(
            myHistoryKind = HistoryItemModel.MyHistoryKind.GainCharacter,
            thumbnailDrawable = R.drawable.img_hatching_characters,
            titleString = R.string.home_hatching_characters,
            unitString = R.string.group_characters_string,
            count = characterCount
        ),
        HistoryItemModel(
            myHistoryKind = HistoryItemModel.MyHistoryKind.SpotArchive,
            thumbnailDrawable = R.drawable.img_spot_history,
            titleString = R.string.home_spot_history,
            unitString = R.string.quantity_string,
            count = spotCount
        )
    )
}

@Composable
private fun HistoryItem(
    itemState: BaseUiState<HistoryItemModel>,
    modifier: Modifier = Modifier,
    onClickItem: (HistoryItemModel.MyHistoryKind) -> Unit
) {
    Column(
        modifier = modifier
            .padding(horizontal = 4.dp)
            .noRippleClickable {
                if (!itemState.isShowShimmer) {
                    onClickItem.invoke(itemState.data.myHistoryKind)
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 비율을 유지하면서 너비에 맞춰 이미지 표시
        val aspectRatio = 69f / 104f

        if (itemState.isShowShimmer) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f / aspectRatio)
                    .clip(RoundedCornerShape(16.dp))
                    .shimmerEffect()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffectGray200()
            )

            Spacer(modifier = Modifier.height(20.dp))
        } else {
            Image(
                painter = painterResource(itemState.data.thumbnailDrawable),
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
                text = stringResource(itemState.data.titleString),
                style = WalkieTheme.typography.head6,
                color = WalkieTheme.colors.gray700
            )
            Text(
                modifier = Modifier.height(20.dp),
                text = stringResource(itemState.data.unitString, itemState.data.count),
                style = WalkieTheme.typography.body2,
                color = WalkieTheme.colors.gray500
            )
        }
    }
}

@Composable
fun EggLayout(
    modifier: Modifier = Modifier,
    stepCountState: BaseUiState<Pair<Int, Int>>,
    eggModelState: BaseUiState<MyEggModel>,
    showActivityRecognitionPermissionAlert: Boolean,
    showBackgroundLocationPermissionAlert: Boolean,
    onNavigationEvent: (HomeScreenNavigationEvent) -> Unit,
    eggAreaHeight: Dp
) {
    val eggAttribute = if (!eggModelState.isShowShimmer) {
        getEggLayoutModel(eggModelState.data.eggKind)
    } else {
        getEggLayoutModel(EggKind.Empty)
    }
    val analyticsHelper = LocalAnalyticsHelper.current
    Box(
        modifier = modifier
            .height(maxOf(eggAreaHeight, 360.dp))
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.3872f to eggAttribute.startGradient,
                        1f to eggAttribute.endGradient
                    )
                )
            )
            .noRippleClickable {
                analyticsHelper.logEvent(EventNameConst.MAIN_BLUE_CARD)
            },
        contentAlignment = Alignment.Center
    ) {
        if (stepCountState.isShowShimmer || eggModelState.isShowShimmer) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shimmerEffect()
            )
        } else {
            val actualBlueAreaHeight = maxOf(eggAreaHeight, 360.dp)

            EggContent(
                eggStep = stepCountState.data.first,
                todayStep = stepCountState.data.second,
                eggModel = eggModelState.data,
                eggAttribute = eggAttribute,
                showActivityRecognitionPermissionAlert = showActivityRecognitionPermissionAlert,
                showBackgroundLocationPermissionAlert = showBackgroundLocationPermissionAlert,
                onNavigationEvent = onNavigationEvent,
                eggBoxHeight = actualBlueAreaHeight
            )
        }
    }
}


@Composable
private fun EggContent(
    todayStep: Int,
    eggStep: Int,
    eggModel: MyEggModel,
    eggAttribute: EggLayoutModel,
    showActivityRecognitionPermissionAlert: Boolean,
    showBackgroundLocationPermissionAlert: Boolean,
    onNavigationEvent: (HomeScreenNavigationEvent) -> Unit,
    eggBoxHeight: Dp
) {

    val eggHeight = eggBoxHeight * 0.57f // 알 높이는 알 영역 높이의 57%
    val eggWidth = eggHeight * 1.3f // 알 너비는 알 높이의 1.3배

    Box(modifier = Modifier.fillMaxSize()) {
        StepInformation(step = todayStep)

        // 이펙트 이미지
        if (eggModel.eggKind != EggKind.Empty) {
            eggAttribute.effectDrawable?.let { drawableRes ->
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(eggBoxHeight * 0.87f) // 알 Box 영역의 87% 높이 적용
                        .align(Alignment.BottomCenter),
                    painter = painterResource(drawableRes),
                    contentDescription = stringResource(R.string.desc_empty_egg),
                    contentScale = ContentScale.FillBounds
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            if (eggModel.eggKind != EggKind.Empty) {
                SpeechBubble(
                    steps = ((eggModel.needStep - eggStep).coerceIn(
                        0,
                        eggModel.needStep
                    )).formatWithLocale(),
                    modifier = Modifier
                        .offset(y = 25.dp)
                        .zIndex(5f),
                )
            }

            // 알 이미지
            Box {
                Image(
                    modifier = Modifier
                        .width(eggWidth)  // 계산된 너비 적용
                        .height(eggHeight), // 계산된 높이 적용
                    painter = painterResource(eggAttribute.eggDrawable),
                    contentDescription = stringResource(R.string.desc_empty_egg),
                    contentScale = ContentScale.Crop
                )

                // 알 선택 텍스트
                if (eggModel.eggKind == EggKind.Empty &&
                    !showActivityRecognitionPermissionAlert &&
                    !showBackgroundLocationPermissionAlert
                ) {
                    Text(
                        text = stringResource(R.string.home_choice_egg).withUnderline(),
                        style = WalkieTheme.typography.head5,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = -eggHeight * 0.3f) // 알 높이의 30%만큼 위로 이동
                            .noRippleClickable {
                                onNavigationEvent.invoke(HomeScreenNavigationEvent.MoveToGainEgg)
                            },
                        color = WalkieTheme.colors.blue50
                    )
                }
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
                text = stringResource(
                    R.string.home_kilometer, kotlin.math.round((0.0006 * step) * 100) / 100.0
                ),
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

@Composable
fun SkeletonHistoryTitleText() {
    Box(
        modifier = Modifier
            .width(104.dp)
            .height(20.dp)
            .clip(RoundedCornerShape(8.dp))
            .shimmerEffect()
    )
}