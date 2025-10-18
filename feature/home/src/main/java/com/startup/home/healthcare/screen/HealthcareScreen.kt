package com.startup.home.healthcare.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.startup.common.animation.StepGoalAchievementAnimation
import com.startup.common.extension.shimmerEffect
import com.startup.common.extension.shimmerEffectGray200
import com.startup.common.extension.shimmerEffectGray50
import com.startup.common.util.DateUtil
import com.startup.common.util.Printer
import com.startup.design_system.ui.WalkieTheme
import com.startup.design_system.ui.noRippleClickable
import com.startup.design_system.widget.actionbar.PageActionBar
import com.startup.design_system.widget.actionbar.PageActionBarType
import com.startup.design_system.widget.badge.EggBadgeStatus
import com.startup.design_system.widget.chart.DonutChart
import com.startup.home.R
import com.startup.home.healthcare.HealthcareUiEvent
import com.startup.home.healthcare.HealthcareViewModel
import com.startup.home.healthcare.HealthcareViewModelEvent
import com.startup.home.healthcare.component.TodayWalkDonutChart
import com.startup.home.healthcare.component.TodayWalkGoalBottomSheet
import com.startup.home.healthcare.component.WalkieWeekHealthcareCalendar
import com.startup.home.spot.component.BottomSheetMonthCalendarComponent
import com.startup.model.egg.EggDetailModel
import com.startup.model.healthcare.CaloriesType
import com.startup.model.healthcare.DailyHealthcareDetailModel
import com.startup.model.spot.CalendarModel
import java.time.LocalDate
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HealthcareScreen(
    healthcareViewModel: HealthcareViewModel = hiltViewModel(),
    onBackPress: () -> Unit
) {
    val monthCalendarSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val todayWalkGoalSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isCalendarBottomModalShow by remember {
        mutableStateOf(false)
    }
    var isTodayWalkGoalBottomModalShow by remember {
        mutableStateOf(false)
    }
    val state = healthcareViewModel.state
    val uiEventSender: (HealthcareUiEvent) -> Unit = {
        healthcareViewModel.notifyViewModelEvent(it)
    }
    val selectedDate by state.currentSelectedDate.collectAsStateWithLifecycle()

    val currentDetail by state.currentDailyHealthcareDetail.collectAsStateWithLifecycle()
    val currentContinueDays by state.currentContinueDays.collectAsStateWithLifecycle()
    val eventMap by state.healthCareList.collectAsStateWithLifecycle()
    val todayTargetStep by state.todayTargetStep.collectAsStateWithLifecycle()
    val eventList = eventMap.entries.flatMap { it.value }
    val today = LocalDate.now()
    var eggDetail: EggDetailModel? by remember { mutableStateOf(null) }

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            healthcareViewModel.event.collect {
                when (it) {
                    is HealthcareViewModelEvent.GetAwardOfEgg -> {
                        eggDetail = it.eggDetail
                    }
                }
            }
        }
    }
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = WalkieTheme.colors.gray50)
        ) {
            PageActionBar(PageActionBarType.JustBackActionBarType(onBackPress))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = WalkieTheme.colors.white)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = DateUtil.convertDateTimeFormat(selectedDate.date),
                    style = WalkieTheme.typography.head2.copy(color = WalkieTheme.colors.gray700)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(R.drawable.ic_calendar),
                    tint = WalkieTheme.colors.gray400,
                    contentDescription = stringResource(R.string.desc_calendar_select),
                    modifier = Modifier.noRippleClickable {
                        isCalendarBottomModalShow = true
                    }
                )
                Spacer(modifier = Modifier.weight(1F))
                if (!today.isEqual(selectedDate.date)) {
                    Text(
                        modifier = Modifier
                            .background(
                                color = WalkieTheme.colors.gray100,
                                shape = RoundedCornerShape(100.dp)
                            )
                            .padding(vertical = 8.dp, horizontal = 12.dp)
                            .noRippleClickable {
                                uiEventSender.invoke(
                                    HealthcareUiEvent.OnDateChanged(
                                        CalendarModel(
                                            today,
                                            true
                                        )
                                    )
                                )
                            },
                        text = stringResource(R.string.date_today),
                        style = WalkieTheme.typography.caption1.copy(color = WalkieTheme.colors.gray500),
                    )
                }
            }
            WalkieWeekHealthcareCalendar(
                selectDate = selectedDate,
                todayTargetStep = todayTargetStep,
                events = eventList,
                onWeekChanged = {
                    uiEventSender.invoke(HealthcareUiEvent.OnDateChanged(CalendarModel(it, false)))
                },
                onDateSelected = {
                    uiEventSender.invoke(HealthcareUiEvent.OnDateChanged(CalendarModel(it, false)))
                },
                onCompleteMove = {
                    uiEventSender.invoke(
                        HealthcareUiEvent.OnDateChanged(
                            CalendarModel(
                                selectedDate.date,
                                false
                            )
                        )
                    )
                })
            Spacer(
                modifier = Modifier
                    .height(4.dp)
                    .fillMaxWidth()
            )
            if (currentDetail.isShowShimmer) {
                HealthcareSkeletonComponent()
            } else {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    HealthcareDetailComponent(
                        dailyHealthcareDetail = currentDetail.data,
                        currentContinueDay = currentContinueDays,
                        isToday = selectedDate.date.isEqual(today),
                        targetStep = if (selectedDate.date.isEqual(today)) {
                            todayTargetStep
                        } else {
                            currentDetail.data.targetSteps
                        },
                        onClickTargetBottomSheet = {
                            isTodayWalkGoalBottomModalShow = true
                        },
                        getEgg = {
                            uiEventSender.invoke(HealthcareUiEvent.GetEgg(selectedDate.date))
                        })
                    Spacer(modifier = Modifier.height(8.dp))
                    CaloriesComponent(currentDetail.data.caloriesType)
                    Spacer(modifier = Modifier.height(38.dp))
                }
            }
        }

        if (isCalendarBottomModalShow) {
            BottomSheetMonthCalendarComponent(
                currentSelectedDate = selectedDate.date,
                onClickCancel = {
                    isCalendarBottomModalShow = false
                },
                onSelectDay = {
                    uiEventSender.invoke(HealthcareUiEvent.OnDateChanged(CalendarModel(it, true)))
                    isCalendarBottomModalShow = false
                },
                sheetState = monthCalendarSheetState
            )
        }
        if (isTodayWalkGoalBottomModalShow) {
            TodayWalkGoalBottomSheet(
                currentGoal = if (selectedDate.date.isEqual(today)) {
                    todayTargetStep
                } else {
                    currentDetail.data.targetSteps
                },
                onClickCancel = {
                    isTodayWalkGoalBottomModalShow = false
                },
                onSelectGoal = {
                    uiEventSender.invoke(HealthcareUiEvent.OnTargetStepChanged(it))
                    isTodayWalkGoalBottomModalShow = false
                },
                sheetState = todayWalkGoalSheetState
            )
        }
        eggDetail?.let { detail ->
            Printer.e("LMH", "CALL!")
            StepGoalAchievementAnimation(
                eggRank = detail.eggKind.kindOfRankId(),
                eggImageResId = detail.eggKind.drawableResId,
                onDismiss = {
                    eggDetail = null
                }
            )
        }
    }
}


@Composable
private fun HealthcareSkeletonComponent() {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .background(WalkieTheme.colors.white, RoundedCornerShape(20.dp))
        ) {
            Row(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(20.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .shimmerEffect()
                )
            }
            HorizontalDivider(thickness = 1.dp, color = WalkieTheme.colors.gray100)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 12.dp),
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopStart),
                    text = stringResource(R.string.healthcare_today_walk_detail),
                    style = WalkieTheme.typography.head5.copy(color = WalkieTheme.colors.gray700),
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DonutChart(
                        modifier = Modifier.size(260.dp),
                        strokeWidth = 26.dp,
                        percentage = 0F
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(20.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .shimmerEffect()
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Box(
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .shimmerEffect()
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        HealthInfoLabelSkeletonComponent(
                            displayTitle = stringResource(R.string.healthcare_move_distance),
                        )
                        HealthInfoLabelSkeletonComponent(
                            displayTitle = stringResource(R.string.healthcare_calories_consume),
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        CaloriesSkeletonComponent()
        Spacer(modifier = Modifier.height(38.dp))
    }
}

@Composable
private fun HealthcareDetailComponent(
    isToday: Boolean,
    currentContinueDay: Int,
    dailyHealthcareDetail: DailyHealthcareDetailModel,
    targetStep: Int,
    onClickTargetBottomSheet: () -> Unit,
    getEgg: () -> Unit,
) {
    val currentContinueDayWithToday = if (isToday && targetStep <= dailyHealthcareDetail.nowSteps) {
        currentContinueDay + 1
    } else {
        currentContinueDay
    }
    val eggBadgeStatus =
        if (isToday && targetStep <= dailyHealthcareDetail.nowSteps && dailyHealthcareDetail.eggBadgeStatus == EggBadgeStatus.PENDING) {
            EggBadgeStatus.AVAILABLE
        } else {
            dailyHealthcareDetail.eggBadgeStatus
        }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
            .background(WalkieTheme.colors.white, RoundedCornerShape(20.dp))
    ) {
        if (currentContinueDayWithToday > 0 && isToday) {
            Row(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
                Image(painter = painterResource(R.drawable.ic_fire), contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text =
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(color = WalkieTheme.colors.blue400)) {
                            append(
                                stringResource(
                                    R.string.healthcare_continue_days,
                                    currentContinueDayWithToday
                                )
                            )
                        }
                        append(stringResource(R.string.healthcare_continue_days_description))
                    },
                    style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray700),
                )
            }
            HorizontalDivider(thickness = 1.dp, color = WalkieTheme.colors.gray100)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 12.dp),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopStart),
                text = stringResource(R.string.healthcare_today_walk_detail),
                style = WalkieTheme.typography.head5.copy(color = WalkieTheme.colors.gray700),
            )

            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .noRippleClickable {
                        if (eggBadgeStatus == EggBadgeStatus.AVAILABLE) {
                            getEgg.invoke()
                        }
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(eggBadgeStatus.eggResId),
                    contentDescription = null
                )
                Text(
                    stringResource(eggBadgeStatus.eggStrResId),
                    style = WalkieTheme.typography.caption1.copy(color = if (eggBadgeStatus == EggBadgeStatus.AVAILABLE) WalkieTheme.colors.blue400 else WalkieTheme.colors.gray500)
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TodayWalkDonutChart(
                    targetStep = targetStep,
                    currentStep = dailyHealthcareDetail.nowSteps,
                    isToday = isToday
                ) {
                    onClickTargetBottomSheet.invoke()
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HealthInfoLabelComponent(
                        displayTitle = stringResource(R.string.healthcare_move_distance),
                        displayLabel = "km",
                        valueStr = String.format(Locale.getDefault(), "%.1f", dailyHealthcareDetail.nowDistance)
                    )
                    HealthInfoLabelComponent(
                        displayTitle = stringResource(R.string.healthcare_calories_consume),
                        displayLabel = "kcal",
                        valueStr = dailyHealthcareDetail.nowCalories.toString()
                    )
                }
            }
        }
    }
}

@Composable
private fun RowScope.HealthInfoLabelComponent(
    displayTitle: String,
    displayLabel: String,
    valueStr: String
) {
    Column(
        modifier = Modifier.weight(1F),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = displayTitle,
            style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray500)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = WalkieTheme.colors.gray50, RoundedCornerShape(8.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.padding(vertical = 4.dp),
                text = valueStr,
                style = WalkieTheme.typography.head3.copy(color = WalkieTheme.colors.gray700)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = displayLabel,
                style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray700)
            )
        }
    }
}

@Composable
private fun RowScope.HealthInfoLabelSkeletonComponent(
    displayTitle: String,
) {
    Column(
        modifier = Modifier
            .weight(1F),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = displayTitle,
            style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray500)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(38.dp)
                .clip(RoundedCornerShape(8.dp))
                .shimmerEffectGray50(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .padding(vertical = 9.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffectGray200(),
            )
        }
    }
}

@Composable
private fun CaloriesComponent(caloriesType: CaloriesType = CaloriesType.Air) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(WalkieTheme.colors.white, RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(caloriesType.imageResId),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                style = WalkieTheme.typography.head6.copy(color = WalkieTheme.colors.gray700),
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = WalkieTheme.colors.blue400)) {
                        append(stringResource(caloriesType.objectNameStrResId))
                    }
                    append(stringResource(R.string.healthcare_calories_title))
                }
            )
            Text(
                style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray500),
                text = stringResource(caloriesType.description)
            )
        }
    }
}

@Composable
private fun CaloriesSkeletonComponent() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(WalkieTheme.colors.white, RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .width(180.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect()
            )
        }
    }
}

@Composable
@PreviewScreenSizes
private fun PreviewHealthcareScreen() {
    WalkieTheme {
        HealthcareDetailComponent(
            isToday = true,
            targetStep = 4000,
            dailyHealthcareDetail = DailyHealthcareDetailModel(
                caloriesType = CaloriesType.Air,
                nowCalories = 100,
                nowDistance = 23.0,
                targetSteps = 4000,
                nowSteps = 4000,
                eggBadgeStatus = EggBadgeStatus.AVAILABLE
            ),
            currentContinueDay = 1,
            onClickTargetBottomSheet = {},
            getEgg = {})
    }
}