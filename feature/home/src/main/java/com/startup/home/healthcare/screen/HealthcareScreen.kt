package com.startup.home.healthcare.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.startup.common.util.DateUtil
import com.startup.design_system.ui.WalkieTheme
import com.startup.design_system.ui.noRippleClickable
import com.startup.design_system.widget.actionbar.PageActionBar
import com.startup.design_system.widget.actionbar.PageActionBarType
import com.startup.home.R
import com.startup.home.healthcare.HealthcareUiEvent
import com.startup.home.healthcare.HealthcareViewState
import com.startup.home.healthcare.HealthcareViewStateImpl
import com.startup.home.healthcare.component.TodayWalkDonutChart
import com.startup.home.healthcare.component.TodayWalkGoalBottomSheet
import com.startup.home.healthcare.component.WalkieWeekHealthcareCalendar
import com.startup.home.spot.component.BottomSheetMonthCalendarComponent
import com.startup.model.healthcare.CaloriesDisplayModel
import com.startup.model.healthcare.DailyHealthcareDetailModel
import com.startup.model.spot.CalendarModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HealthcareScreen(
    state: HealthcareViewState,
    uiEventSender: (HealthcareUiEvent) -> Unit,
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
    val selectedDate by state.currentSelectedDate.collectAsStateWithLifecycle()

    val currentDetail by state.currentDailyHealthcareDetail.collectAsStateWithLifecycle()
    val currentContinueDays by state.currentContinueDays.collectAsStateWithLifecycle()
    val eventMap by state.healthCareList.collectAsStateWithLifecycle()
    val eventList = eventMap.entries.flatMap { it.value }
    val today = LocalDate.now()

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
            // TODO 스켈레톤
        } else {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 4.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                HealthcareDetailComponent(
                    dailyHealthcareDetail = currentDetail.data,
                    currentContinueDay = currentContinueDays,
                    isToday = selectedDate.date == today,
                    onClickTargetBottomSheet = {
                        isTodayWalkGoalBottomModalShow = true
                    })
                Spacer(modifier = Modifier.height(8.dp))
                CaloriesComponent(currentDetail.data.caloriesDisplayModel)
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
            currentGoal = currentDetail.data.targetSteps,
            onClickCancel = {
                isTodayWalkGoalBottomModalShow = false
            },
            onSelectGoal = {
                uiEventSender.invoke(HealthcareUiEvent.OnGoalChanged(it))
                isTodayWalkGoalBottomModalShow = false
            },
            sheetState = todayWalkGoalSheetState
        )
    }
}


@Composable
private fun HealthcareDetailComponent(
    isToday: Boolean,
    currentContinueDay: Int,
    dailyHealthcareDetail: DailyHealthcareDetailModel,
    onClickTargetBottomSheet: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(WalkieTheme.colors.white, RoundedCornerShape(20.dp))
    ) {
        if (currentContinueDay > 0 && isToday) {
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
                                    currentContinueDay
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
            if (dailyHealthcareDetail.targetSteps > 0 && dailyHealthcareDetail.targetSteps >= dailyHealthcareDetail.nowSteps) {
                Column(
                    modifier = Modifier
                        .align(Alignment.TopEnd),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(painter = painterResource(R.drawable.ic_firebadge), contentDescription = null)
                    Text(
                        stringResource(R.string.healthcare_achieve),
                        style = WalkieTheme.typography.caption1.copy(color = WalkieTheme.colors.blue400)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 48.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TodayWalkDonutChart(
                    targetStep = dailyHealthcareDetail.targetSteps,
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
                        valueStr = dailyHealthcareDetail.nowDistance.toString()
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
private fun CaloriesComponent(caloriesDisplayModel: CaloriesDisplayModel = CaloriesDisplayModel.orEmpty()) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(WalkieTheme.colors.white, RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = caloriesDisplayModel.url,
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
                        append(caloriesDisplayModel.title)
                    }
                    append(stringResource(R.string.healthcare_calories_title))
                }
            )
            Text(
                style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray500),
                text = caloriesDisplayModel.description
            )
        }
    }
}

@Composable
@PreviewScreenSizes
private fun PreviewHealthcareScreen() {
    WalkieTheme {
        HealthcareScreen(
            HealthcareViewStateImpl(currentContinueDays = MutableStateFlow(0)), {}
        ) {}
    }
}