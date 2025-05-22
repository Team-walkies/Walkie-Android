package com.startup.home.spot.screen

import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.startup.common.extension.shimmerEffect
import com.startup.common.extension.shimmerEffectGray200
import com.startup.common.util.DateUtil
import com.startup.common.util.DateUtil.getStartOfWeek
import com.startup.common.util.ExtraConst
import com.startup.design_system.widget.actionbar.PageActionBar
import com.startup.design_system.widget.actionbar.PageActionBarType
import com.startup.design_system.widget.review.RatingSmallView
import com.startup.home.R
import com.startup.model.spot.CalendarModel
import com.startup.model.spot.ReviewModel
import com.startup.design_system.ui.WalkieTheme
import com.startup.design_system.ui.noRippleClickable
import com.startup.ga.EventNameConst
import com.startup.ga.LocalAnalyticsHelper
import com.startup.ga.logEvent
import com.startup.home.spot.component.BottomSheetMonthCalendarComponent
import com.startup.home.spot.component.BottomSheetReviewOption
import com.startup.home.spot.SpotArchiveUiEvent
import com.startup.home.spot.SpotArchiveViewState
import com.startup.home.spot.SpotArchiveViewStateImpl
import com.startup.home.spot.component.WalkieWeekCalendar
import com.startup.model.character.WalkieCharacter
import com.startup.model.spot.SpotKeyword
import kotlinx.coroutines.flow.map
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SpotArchiveScreen(
    state: SpotArchiveViewState,
    uiEventSender: (SpotArchiveUiEvent) -> Unit,
) {
    val monthCalendarSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val optionSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isCalendarBottomModalShow by remember {
        mutableStateOf(false)
    }
    val selectedDate by state.currentSelectedDate.collectAsStateWithLifecycle()
    val reviews by state.eventList.map { eventMap ->
        eventMap["${getStartOfWeek(selectedDate.date)}"]?.data?.filter { it.date == selectedDate.date } ?: emptyList()
    }.collectAsStateWithLifecycle(emptyList())
    val isShimmer by state.eventList.map { it["${getStartOfWeek(selectedDate.date)}"]?.isShowShimmer ?: false }
        .collectAsStateWithLifecycle(true)

    val eventList by state.eventList.map { eventMap ->
        eventMap.entries.flatMap { it.value.data }
    }.collectAsStateWithLifecycle(emptyList())
    var selectedOptionOfReview: ReviewModel? by remember {
        mutableStateOf(null)
    }

    // 장소
    val modifyReviewLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                uiEventSender.invoke(SpotArchiveUiEvent.RefreshReviewList)
            }
        }
    val today = LocalDate.now()
    val analyticsHelper = LocalAnalyticsHelper.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = WalkieTheme.colors.white)
    ) {
        PageActionBar(PageActionBarType.JustBackActionBarType { uiEventSender.invoke(
            SpotArchiveUiEvent.OnBack
        ) })
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(vertical = 1.dp),
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
                                SpotArchiveUiEvent.OnDateChanged(
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
        Spacer(modifier = Modifier.height(12.dp))
        WalkieWeekCalendar(
            selectDate = selectedDate,
            events = eventList.map { it.date },
            onWeekChanged = {
                uiEventSender.invoke(SpotArchiveUiEvent.OnDateChanged(CalendarModel(it, false)))
            },
            onDateSelected = {
                uiEventSender.invoke(SpotArchiveUiEvent.OnDateChanged(CalendarModel(it, false)))
            },
            onCompleteMove = {
                uiEventSender.invoke(SpotArchiveUiEvent.OnDateChanged(CalendarModel(selectedDate.date, false)))
            })
        Spacer(modifier = Modifier.height(11.dp))
        HorizontalDivider(thickness = 4.dp, color = WalkieTheme.colors.gray50)
        Spacer(modifier = Modifier.height(12.dp))

        if (isShimmer) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
            ) {
                item { SkeletonHistoryTitle() }
                item { Spacer(modifier = Modifier.height(12.dp)) }
                items(4) {
                    SkeletonReviewItem()
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        } else {
            if (reviews.isEmpty()) {
                EmptyReviewView()
            } else {
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
                                text = "${reviews.size}",
                                style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray500)
                            )
                        }
                    }
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
    }
    if (isCalendarBottomModalShow) {
        BottomSheetMonthCalendarComponent(
            currentSelectedDate = selectedDate.date,
            onClickCancel = {
                isCalendarBottomModalShow = false
            },
            onSelectDay = {
                analyticsHelper.logEvent(EventNameConst.SPOT_CALENDAR_MOVE)
                uiEventSender.invoke(SpotArchiveUiEvent.OnDateChanged(CalendarModel(it, true)))
                isCalendarBottomModalShow = false
            },
            sheetState = monthCalendarSheetState
        )
    }
    if (selectedOptionOfReview != null) {
        selectedOptionOfReview?.let {
            BottomSheetReviewOption(
                onClickCancel = {
                    selectedOptionOfReview = null
                },
                onClickDelete = {
                    uiEventSender.invoke(SpotArchiveUiEvent.OnDeleteReview(review = it))
                    selectedOptionOfReview = null
                },
                onClickModify = {
                    selectedOptionOfReview = null
                    uiEventSender.invoke(SpotArchiveUiEvent.OnModifyReview(modifyReviewLauncher) {
                        putExtra(ExtraConst.EXTRA_REVIEW_ID, it.reviewId)
                        putExtra(ExtraConst.EXTRA_SPOT_ID, it.spotId)
                    })
                },
                sheetState = optionSheetState
            )
        }
    }
}

@Composable
private fun EmptyReviewView() {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.calendar_review_title),
                style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray500)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "0",
                style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray500)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier.padding(top = 170.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.calendar_review_empty),
                style = WalkieTheme.typography.body1.copy(color = WalkieTheme.colors.gray400),
            )
        }
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
                painter = painterResource(reviewModel.walkieCharacter.characterImageResId),
                contentDescription = stringResource(R.string.desc_character)
            )
            Column(modifier = Modifier.weight(1F)) {
                Row {
                    Text(
                        text = stringResource(reviewModel.walkieCharacter.characterNameResId),
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
                    painter = painterResource(reviewModel.spotType.drawableResId),
                    contentDescription = stringResource(R.string.desc_calendar_review_map_kind)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = reviewModel.spotName,
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
private fun SkeletonReviewItem() {
    Column(
        modifier = Modifier
            .background(color = WalkieTheme.colors.white)
            .fillMaxWidth()
    ) {
        // 프로필 영역
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(100.dp, 20.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.weight(1F))
            Icon(
                modifier = Modifier
                    .size(24.dp),
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .padding(horizontal = 74.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .shimmerEffect()
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                thickness = 1.dp,
                color = WalkieTheme.colors.gray200
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .weight(1F)
                        .height(20.dp)
                        .padding(horizontal = 10.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                        .shimmerEffect()
                )
                Box(
                    modifier = Modifier
                        .weight(1F)
                        .height(20.dp)
                        .padding(horizontal = 10.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                        .shimmerEffect()
                )
                Box(
                    modifier = Modifier
                        .weight(1F)
                        .height(20.dp)
                        .padding(horizontal = 10.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                        .shimmerEffect()
                )
            }
        }
        SkeletonRatingWithReviewComponent()
    }
}

@Composable
private fun SkeletonRatingWithReviewComponent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clip(shape = RoundedCornerShape(12.dp))
            .shimmerEffect()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Box(
            modifier = Modifier
                .size(80.dp, 20.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .shimmerEffectGray200()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .shimmerEffectGray200(),
        )
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
                walkieCharacter = WalkieCharacter.ofEmpty(),
                step = "2,000",
                spotId = 1,
                spotType = SpotKeyword.FOOD,
                spotName = "",
                distance = 23.toDouble()
            ),
            {}
        )
    }
}

@Composable
private fun SkeletonHistoryTitle() {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.calendar_review_title),
            style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray500)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .shimmerEffect()
        )
    }
}

@Composable
@Preview
private fun PreviewSkeleton() {
    WalkieTheme {
        SkeletonReviewItem()
    }
}

@Composable
@Preview
private fun PreviewSpotArchiveScreen() {
    WalkieTheme {
        SpotArchiveScreen(
            SpotArchiveViewStateImpl()
        ) {}
    }
}