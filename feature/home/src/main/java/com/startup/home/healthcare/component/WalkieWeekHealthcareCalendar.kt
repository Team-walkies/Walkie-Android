package com.startup.home.healthcare.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.common.util.DateUtil.getStartOfWeek
import com.startup.common.util.DateUtil.matchWeekdayInSameWeekUntilToday
import com.startup.common.util.Printer
import com.startup.design_system.ui.WalkieTheme
import com.startup.design_system.ui.noRippleClickable
import com.startup.model.healthcare.DailyHealthcareListItemModel
import com.startup.model.spot.CalendarModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun WalkieWeekHealthcareCalendar(
    selectDate: CalendarModel,
    todayTargetStep: Int,
    events: List<DailyHealthcareListItemModel>,
    onWeekChanged: (LocalDate) -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    onCompleteMove: () -> Unit,
) {
    val latestSelectDate by rememberUpdatedState(newValue = selectDate)
    val today = LocalDate.now()
    val currentWeekFirstDay = getStartOfWeek(latestSelectDate.date)
    val pagerState = rememberPagerState(
        initialPage = Int.MAX_VALUE,
        pageCount = { Int.MAX_VALUE }
    )
    LaunchedEffect(selectDate) {
        if (selectDate.isSpecificDate) {
            val weeksDifference = ChronoUnit.WEEKS.between(currentWeekFirstDay, today)
            if ((pagerState.currentPage + 1) != (Int.MAX_VALUE) - (weeksDifference.toInt())) {
                Printer.e("LMH", "WEEK $weeksDifference")
                kotlin.runCatching {
                    pagerState.animateScrollToPage(((Int.MAX_VALUE) - (weeksDifference.toInt()) - 1))
                    onCompleteMove.invoke()
                }
            } else {
                onCompleteMove.invoke()
            }
        }
    }
    LaunchedEffect(Unit) {
        snapshotFlow { pagerState.currentPage to pagerState.isScrollInProgress }
            .filter { (_, isScrolling) -> !isScrolling } // 스크롤 끝났을 때만
            .map { (page, _) -> page }
            .distinctUntilChanged()
            .collect { page ->
                Printer.e("LMH", "PAGE! $page, $latestSelectDate")
                if (!latestSelectDate.isSpecificDate) {
                    val weekChangeOfToDay =
                        today.plusWeeks((page.toLong() + 1) - (Int.MAX_VALUE.toLong()))


                    val latestWeekFirstDay = getStartOfWeek(latestSelectDate.date)
                    if (!latestWeekFirstDay.isEqual(getStartOfWeek(weekChangeOfToDay))) {
                        val alignWeekDate = matchWeekdayInSameWeekUntilToday(
                            from = latestSelectDate.date,
                            to = weekChangeOfToDay,
                            today = today
                        )
                        Printer.e("LMH", "WEEK CHANGED $alignWeekDate")
                        onWeekChanged(alignWeekDate)
                    }
                }
            }
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = WalkieTheme.colors.white,
                shape = RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp)
            )
            .padding(horizontal = 16.dp),
    ) { page ->
        val startOfWeek =
            getStartOfWeek(today.plusWeeks((page.toLong() + 1) - Int.MAX_VALUE.toLong()))
        WeeklyView(
            todayTargetStep = todayTargetStep,
            startOfWeek = startOfWeek,
            today = today,
            selectedDate = latestSelectDate.date,
            events = events,
            onDateSelected = { date ->
                onDateSelected(date)
            }
        )
    }
}


@Composable
private fun WeeklyView(
    todayTargetStep: Int,
    startOfWeek: LocalDate,
    today: LocalDate,
    selectedDate: LocalDate,
    events: List<DailyHealthcareListItemModel>,
    onDateSelected: (LocalDate) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(), // 주간 크기 고정
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        (0 until 7).map { startOfWeek.plusDays(it.toLong()) }.forEach { date ->
            val isSelected = date == selectedDate
            val healthCareData = events.find { it.date == date } ?: DailyHealthcareListItemModel.orEmpty()
            val isFuture = date > today
            val textColor =
                if (today == date) WalkieTheme.colors.blue400 else if (isFuture) WalkieTheme.colors.gray300 else WalkieTheme.colors.gray700
            Column(
                modifier = Modifier
                    .weight(1F)
                    .padding(bottom = 5.dp)
                    .noRippleClickable(enabled = !isFuture) {
                        onDateSelected(date)
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(max = 45.dp)
                        .fillMaxWidth()
                        .background(
                            color = when {
                                !isSelected -> Color.Transparent
                                date == selectedDate -> WalkieTheme.colors.blue30
                                else -> WalkieTheme.colors.gray100
                            },
                            RoundedCornerShape(8.dp)
                        )
                        .padding(vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = date.format(DateTimeFormatter.ofPattern("E")),
                        style = WalkieTheme.typography.body2.copy(color = textColor)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Box(
                        modifier = Modifier,
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = date.dayOfMonth.toString(),
                            style = WalkieTheme.typography.head5.copy(color = textColor),
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                DailyStepGoalDonutChart(
                    percentage = healthCareData.nowSteps.toFloat() /
                            if (date.isEqual(today)) {
                                todayTargetStep
                            } else {
                                healthCareData.targetSteps
                            }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewWalkieWeekHealthcareCalendar() {
    WalkieTheme {
        WalkieWeekHealthcareCalendar(
            selectDate = CalendarModel(date = LocalDate.now(), false),
            todayTargetStep = 6_000,
            events = listOf(DailyHealthcareListItemModel(nowSteps = 300, LocalDate.now(), targetSteps = 1000)),
            onWeekChanged = {},
            onCompleteMove = {},
            onDateSelected = {}
        )
    }
}