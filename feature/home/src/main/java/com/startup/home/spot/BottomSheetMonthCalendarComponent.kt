package com.startup.home.spot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.common.util.EMPTY_STRING
import com.startup.design_system.widget.bottom_sheet.WalkieDragHandle
import com.startup.design_system.widget.button.PrimaryButton
import com.startup.home.R
import com.startup.home.spot.model.MonthType
import com.startup.home.spot.model.WeekendType
import com.startup.ui.WalkieTheme
import com.startup.ui.noRippleClickable
import java.time.LocalDate

/** 바텀 시트, Preview 를 위해 뷰 파일과 분리 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BottomSheetMonthCalendarComponent(
    currentSelectedDate: LocalDate,
    onClickCancel: () -> Unit,
    onSelectDay: (LocalDate) -> Unit,
    sheetState: SheetState
) {
    WalkieTheme {
        ModalBottomSheet(
            onDismissRequest = { onClickCancel() },
            sheetState = sheetState,
            tonalElevation = 24.dp,
            containerColor = WalkieTheme.colors.white,
            dragHandle = {
                WalkieDragHandle()
            },
            contentColor = WalkieTheme.colors.white,
            scrimColor = WalkieTheme.colors.blackOpacity60,
        ) {
            BottomSheetCalendarContent(
                selectDateTime = currentSelectedDate,
                onClickDate = {
                    onSelectDay.invoke(it)
                })
        }
    }
}


@Composable
private fun BottomSheetCalendarContent(
    selectDateTime: LocalDate,
    onClickDate: (LocalDate) -> Unit
) {
    var selectDate by remember {
        mutableStateOf(selectDateTime)
    }
    var currentDate by remember {
        mutableStateOf(selectDateTime)
    }
    val today = LocalDate.now()
    var isNextMonthEnabled by remember { mutableStateOf(today.isAfter(currentDate)) }
    LaunchedEffect(currentDate.month) {
        isNextMonthEnabled = today.isAfter(currentDate)
        val targetDay = selectDate.dayOfMonth
        val lastDayOfNewMonth = currentDate.lengthOfMonth()
        val correctedDay = minOf(targetDay, lastDayOfNewMonth)
        val candidateDate = currentDate.withDayOfMonth(correctedDay)
        // 오늘보다 미래면 today와 비교해서 이전 날짜로 보정
        selectDate = if (candidateDate.isAfter(today)) {
            if (today.month == currentDate.month && today.year == currentDate.year) {
                today // 같은 월이면 today까지만 선택
            } else {
                currentDate.withDayOfMonth(minOf(today.dayOfMonth, lastDayOfNewMonth))
            }
        } else {
            candidateDate
        }
    }
    Column(
        modifier = Modifier
            .background(color = WalkieTheme.colors.white)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${currentDate.year}년 " + "${MonthType.entries[currentDate.monthValue - 1].month}월",
                style = WalkieTheme.typography.head4.copy(color = WalkieTheme.colors.gray700),
            )
            Row {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_left),
                    modifier = Modifier
                        .size(24.dp)
                        .noRippleClickable {
                            currentDate = currentDate.minusMonths(1)
                        },
                    tint = WalkieTheme.colors.blue300,
                    contentDescription = stringResource(R.string.desc_calendar_previous)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    modifier = Modifier
                        .size(24.dp)
                        .noRippleClickable {
                            if (isNextMonthEnabled) {
                                currentDate = currentDate.plusMonths(1)
                            }
                        },
                    tint = if (!isNextMonthEnabled) {
                        WalkieTheme.colors.gray300
                    } else {
                        WalkieTheme.colors.blue300
                    },
                    contentDescription = stringResource(R.string.desc_calendar_after)
                )
            }
        }
        WalkieCalendar(
            currentDate = currentDate,
            selectValue = selectDate,
            today = today,
        ) {
            if (it != null && it.isBefore(today)) {
                selectDate = it
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        PrimaryButton(text = stringResource(R.string.calendar_date_select), onClick = {
            onClickDate.invoke(selectDate)
        })
        Spacer(modifier = Modifier.height(28.dp))
    }
}

/** 캘린더 컴포넌트 */
@Composable
private fun WalkieCalendar(
    currentDate: LocalDate,
    selectValue: LocalDate,
    today: LocalDate,
    onClick: (LocalDate?) -> Unit
) {
    var day = LocalDate.of(currentDate.year, currentDate.monthValue, 1)
    val startDay = LocalDate.of(currentDate.year, currentDate.monthValue, 1).dayOfWeek.value % 7
    Row {
        WeekendType.entries.forEach {
            Text(
                text = stringResource(it.weekDisplayStrResId),
                style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray400),
                modifier = Modifier.weight(1F),
                textAlign = TextAlign.Center,
            )
        }
    }
    Spacer(modifier = Modifier.height(7.dp))
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        repeat(6) { column ->
            Row(Modifier.fillMaxWidth()) {
                repeat(7) { row ->
                    if ((column == 0 && startDay > row) || (day.dayOfMonth == day.lengthOfMonth())) {
                        CalendarLabelText(
                            date = null,
                            isOutOfToday = day.isAfter(today),
                            isSelected = false,
                        ) { onClick(null) }
                    } else {
                        CalendarLabelText(
                            date = day,
                            isOutOfToday = day.isAfter(today),
                            isSelected = selectValue.dayOfYear == day.dayOfYear,
                        ) { day ->
                            onClick(day)
                        }
                        day = day.plusDays(1)
                    }
                }
            }
        }
    }
}

/** 날짜가 들어갈 컴포넌트 */
@Composable
private fun RowScope.CalendarLabelText(
    date: LocalDate?,
    isSelected: Boolean,
    isOutOfToday: Boolean,
    onClickLabel: (LocalDate?) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .weight(1F)
            .height(32.dp)
            .noRippleClickable { onClickLabel(date) }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(32.dp)
                .then(
                    if (isSelected) {
                        Modifier
                            .background(WalkieTheme.colors.blue300, shape = CircleShape)
                    } else {
                        Modifier
                    }
                )
        ) {
            Text(
                text = date?.dayOfMonth?.toString() ?: EMPTY_STRING,
                style = WalkieTheme.typography.body1.copy(
                    color = if (isOutOfToday) {
                        WalkieTheme.colors.gray300
                    } else if (isSelected) {
                        WalkieTheme.colors.white
                    } else {
                        WalkieTheme.colors.gray500
                    }
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}


@Preview
@Composable
private fun PreviewBottomModal() {
    WalkieTheme {
        BottomSheetCalendarContent(selectDateTime = LocalDate.now(), onClickDate = {})
    }
}
