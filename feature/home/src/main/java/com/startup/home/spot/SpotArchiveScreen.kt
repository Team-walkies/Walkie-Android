package com.startup.home.spot

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.startup.common.util.DateUtil
import com.startup.common.util.DateUtil.getStartOfWeek
import com.startup.design_system.widget.actionbar.PageActionBar
import com.startup.design_system.widget.actionbar.PageActionBarType
import com.startup.design_system.widget.review.RatingSmallView
import com.startup.home.R
import com.startup.home.spot.model.CalendarModel
import com.startup.home.spot.model.ReviewModel
import com.startup.home.spot.model.SpotArchiveViewState
import com.startup.home.spot.model.SpotArchiveViewStateImpl
import com.startup.ui.WalkieTheme
import com.startup.ui.noRippleClickable
import kotlinx.coroutines.flow.map
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SpotArchiveScreen(
    state: SpotArchiveViewState,
    onDateUpdate: (CalendarModel) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isCalendarBottomModalShow by remember {
        mutableStateOf(false)
    }
    val selectedDate by state.currentSelectedDate.collectAsStateWithLifecycle()
    val reviews by state.eventList.map { eventMap ->
        eventMap["${selectedDate.date.year}${selectedDate.date.month}"] ?: emptyList()
    }.collectAsStateWithLifecycle(emptyList())
    var selectedOptionOfReview: ReviewModel? by remember {
        mutableStateOf(null)
    }
    val today = LocalDate.now()
    var displayDateWeekFirstDay: LocalDate by remember { mutableStateOf(getStartOfWeek(today = today)) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = WalkieTheme.colors.white)
    ) {
        PageActionBar(PageActionBarType.JustBackActionBarType({}))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(vertical = 1.dp),
                text = DateUtil.convertDateTimeFormat(displayDateWeekFirstDay),
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
                            val weekFirstDay = getStartOfWeek(today)
                            displayDateWeekFirstDay = weekFirstDay
                            onDateUpdate.invoke(CalendarModel(today, true))
                        },
                    text = stringResource(R.string.date_today),
                    style = WalkieTheme.typography.caption1.copy(color = WalkieTheme.colors.gray500),
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        WalkieWeekCalendar(
            events = reviews.map { it.date },
            onWeekChanged = {
                displayDateWeekFirstDay = getStartOfWeek(it)
                onDateUpdate.invoke(CalendarModel(it, false))
            },
            selectDate = selectedDate,
            onDateSelected = {
                onDateUpdate.invoke(CalendarModel(it, false))
            },
            onCompleteMove = {
                onDateUpdate.invoke(CalendarModel(selectedDate.date, false))
            })
        Spacer(modifier = Modifier.height(11.dp))
        HorizontalDivider(thickness = 4.dp, color = WalkieTheme.colors.gray50)
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 16.dp),
        ) {
            item {
                Row {
                    Text(
                        text = stringResource(R.string.calendar_review_title),
                        style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray500)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = " ${reviews.size}",
                        style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray500)
                    )
                }
            }
            if (reviews.isEmpty()) {
                item {
                    EmptyReviewView()
                }
            } else {
                item { Spacer(modifier = Modifier.height(12.dp)) }
                items(reviews) { item ->
                    ReviewItem(item) {
                        selectedOptionOfReview = it
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
    if (isCalendarBottomModalShow) {
        BottomSheetCalendarComponent(
            currentSelectedDate = selectedDate.date,
            onClickCancel = {
                isCalendarBottomModalShow = false
            },
            onSelectDay = {
                val weekFirstDay = getStartOfWeek(it)
                displayDateWeekFirstDay = weekFirstDay
                onDateUpdate.invoke(CalendarModel(it, true))
                isCalendarBottomModalShow = false
            },
            sheetState = sheetState
        )
    }
    if (selectedOptionOfReview != null) {

    }
}

@Composable
private fun EmptyReviewView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.calendar_review_empty),
            style = WalkieTheme.typography.body1.copy(color = WalkieTheme.colors.gray400),
        )
    }
}


@Composable
private fun ReviewItem(reviewModel: ReviewModel, onClickOption: (ReviewModel) -> Unit) {
    Column(
        modifier = Modifier
            .background(color = WalkieTheme.colors.white)
            .fillMaxWidth()
    ) {
        // 프로필 영역
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier.size(38.dp),
                painter = painterResource(R.drawable.jelly_1),
                contentDescription = stringResource(R.string.desc_character)
            )
            Column(modifier = Modifier.weight(1F)) {
                Row {
                    Text(
                        text = "해파리",
                        style = WalkieTheme.typography.head6.copy(color = WalkieTheme.colors.gray700)
                    )
                    Text(
                        text = stringResource(R.string.calendar_review_walk_together),
                        style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray500)
                    )
                }
                Text(
                    reviewModel.timeRange,
                    style = WalkieTheme.typography.caption1.copy(color = WalkieTheme.colors.gray500)
                )
            }
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .noRippleClickable {
                        onClickOption.invoke(reviewModel)
                    },
                painter = painterResource(R.drawable.ic_more),
                contentDescription = stringResource(R.string.desc_calendar_review_option),
                tint = WalkieTheme.colors.gray400
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        // 리뷰 영역
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    1.dp,
                    color = WalkieTheme.colors.gray200,
                    shape = RoundedCornerShape(12.dp)
                )
                .background(color = WalkieTheme.colors.white, shape = RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(R.drawable.ic_map_park),
                    contentDescription = stringResource(R.string.desc_calendar_review_map_kind)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = reviewModel.spotId.toString(),
                    style = WalkieTheme.typography.head6.copy(color = WalkieTheme.colors.blue400)
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                thickness = 1.dp,
                color = WalkieTheme.colors.gray200
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.weight(1F),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.calendar_review_step_title),
                        style = WalkieTheme.typography.caption1.copy(WalkieTheme.colors.gray500)
                    )
                    Text(
                        text = reviewModel.distance.toString(),
                        style = WalkieTheme.typography.head5.copy(WalkieTheme.colors.gray700)
                    )
                }
                Column(
                    modifier = Modifier.weight(1F),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.calendar_review_step_count),
                        style = WalkieTheme.typography.caption1.copy(WalkieTheme.colors.gray500)
                    )
                    Text(
                        text = reviewModel.step,
                        style = WalkieTheme.typography.head5.copy(WalkieTheme.colors.gray700)
                    )
                }
                Column(
                    modifier = Modifier.weight(1F),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.calendar_review_step_duration),
                        style = WalkieTheme.typography.caption1.copy(WalkieTheme.colors.gray500)
                    )
                    Text(
                        text = reviewModel.moveDuration,
                        style = WalkieTheme.typography.head5.copy(WalkieTheme.colors.gray700)
                    )
                }
            }
        }

        // 평점 영역
        if (reviewModel.reviewCd) {
            RatingWithReviewComponent(reviewModel.review, reviewModel.rating)
        }
    }
}

@Composable
private fun RatingWithReviewComponent(content: String, rating: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .background(WalkieTheme.colors.gray100, shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        RatingSmallView(rating = rating)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = content,
            style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray700)
        )
    }
}

@Preview
@Composable
private fun PreviewReviewItem() {
    WalkieTheme {
        ReviewItem(
            ReviewModel(
                reviewId = 1,
                reviewCd = true,
                review = "하이",
                date = LocalDate.now(),
                pic = "",
                timeRange = "오후 01:45 ~ 04:10",
                rating = 2,
                moveDuration = "10h 20m",
                characterId = 1,
                step = "2,000",
                spotId = 1,
                distance = 23.toDouble()
            ),
            {}
        )
    }
}


@Composable
@Preview
private fun PreviewSpotArchiveScreen() {
    WalkieTheme {
        SpotArchiveScreen(
            SpotArchiveViewStateImpl(),
            onDateUpdate = {}
        )
    }
}