package com.startup.home.spot.model

import com.startup.common.util.DateUtil
import com.startup.common.util.formatWithLocale
import com.startup.domain.model.review.Review
import com.startup.home.character.model.WalkieCharacter
import java.time.LocalDate

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
    /** 리뷰 사진 url */
    val pic: String,
    /** 리뷰 작성 여부 */
    val reviewCd: Boolean,
    val review: String,
    val walkieCharacter: WalkieCharacter,
    val spotType: SpotKeyword,
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
            review = review,
            reviewCd = reviewCd,
            pic = pic,
            walkieCharacter = WalkieCharacter.getTypeAndClassOfWalkieCharacter(
                type = characterType,
                rank = rank,
                characterId = characterId,
                clazz = characterClass,
                picked = false,
                count = 0
            ),
            spotType = try {
                SpotKeyword.valueOf(spotType)
            } catch (e: IllegalArgumentException) {
                SpotKeyword.NATURE
            },
            rating = rating
        )
    }
}
