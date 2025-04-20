package com.startup.home.spot

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import com.startup.common.util.DateUtil.getStartOfWeek
import com.startup.common.util.DateUtil.matchWeekdayInSameWeekUntilToday
import com.startup.common.util.Printer
import com.startup.home.spot.model.CalendarModel
import com.startup.ui.WalkieTheme
import com.startup.ui.noRippleClickable
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
internal fun WalkieWeekCalendar(
    selectDate: CalendarModel,
    events: List<LocalDate>,
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
            .padding(horizontal = 16.dp)
            .background(color = WalkieTheme.colors.white),
    ) { page ->
        val startOfWeek =
            getStartOfWeek(today.plusWeeks((page.toLong() + 1) - Int.MAX_VALUE.toLong()))
        WeeklyView(
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
    startOfWeek: LocalDate,
    today: LocalDate,
    selectedDate: LocalDate,
    events: List<LocalDate>,
    onDateSelected: (LocalDate) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(), // 주간 크기 고정
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        (0 until 7).map { startOfWeek.plusDays(it.toLong()) }.forEach { date ->
            val isSelected = date == selectedDate
            val hasEvent = events.contains(date)
            val isFuture = date > today
            val textColor =
                if (isSelected) WalkieTheme.colors.white else if (isFuture) WalkieTheme.colors.gray300 else WalkieTheme.colors.gray700
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1F)
                    .padding(bottom = 5.dp)
                    .background(
                        color = when {
                            !isSelected -> Color.Transparent
                            today == selectedDate -> WalkieTheme.colors.blue300
                            else -> WalkieTheme.colors.gray600
                        },
                        RoundedCornerShape(12.dp)
                    )
                    .padding(top = 6.dp)
            ) {
                Text(
                    text = date.format(DateTimeFormatter.ofPattern("E")),
                    style = WalkieTheme.typography.body2.copy(color = textColor)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .noRippleClickable(enabled = !isFuture) {
                            onDateSelected(date)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        style = WalkieTheme.typography.head5.copy(color = textColor),
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    if (hasEvent) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .offset(y = 5.dp)
                                .border(1.dp, color = WalkieTheme.colors.white, CircleShape)
                                .background(WalkieTheme.colors.blue300, CircleShape)
                                .align(Alignment.BottomCenter)
                        )
                    }
                }
            }
        }
    }
}
