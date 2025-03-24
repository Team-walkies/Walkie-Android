package com.startup.home.spot.model

import com.startup.common.util.DateUtil
import com.startup.common.util.formatWithLocale
import com.startup.domain.model.review.Review
import java.time.LocalDate
import java.time.LocalTime

data class ReviewModel(
    val reviewId: Int,
    val spotId: Int,
    /** 이동 거리 */
    val distance: Double,
    val step: String,
    val date: LocalDate,
    val timeRange: String,
    /** 이동 시간 */
    val moveDuration: String,
    /** 같이 걸은 캐릭터 Id */
    val characterId: Int,
    /** 리뷰 사진 url */
    val pic: String,
    /** 리뷰 작성 여부 */
    val reviewCd: Boolean,
    val review: String,
    /** 평점 */
    val rating: Int,
) {
    companion object {
        fun Review.toUiModel(): ReviewModel = ReviewModel(
            reviewId = reviewId,
            spotId = spotId,
            distance = distance,
            step = step.formatWithLocale(),
            timeRange = DateUtil.formatTimeRange(startTime, endTime),
            moveDuration = DateUtil.convertTimeBetweenDuration(startTime, endTime),
            date = DateUtil.convertLocalDate(date),
            characterId = characterId,
            review = review,
            reviewCd = reviewCd,
            pic = pic,
            rating = rating
        )
    }
}
