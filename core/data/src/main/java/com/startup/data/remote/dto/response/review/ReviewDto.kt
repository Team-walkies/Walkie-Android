package com.startup.data.remote.dto.response.review

import com.google.gson.annotations.SerializedName
import com.startup.common.extension.orFalse
import com.startup.common.extension.orZero
import com.startup.domain.model.review.Review

data class ReviewDto(
    @SerializedName("reviewId")
    val reviewId: Int?,
    @SerializedName("spotId")
    val spotId: Int?,
    @SerializedName("distance")
    val distance: Double?,
    @SerializedName("step")
    val step: Int?,
    /** yyyy-MM-dd */
    @SerializedName("date")
    val date: String?,
    /** hh:mm:ss */
    @SerializedName("startTime")
    val startTime: String?,
    /** hh:mm:ss */
    @SerializedName("endTime")
    val endTime: String?,
    @SerializedName("characterId")
    val characterId: Long?,
    @SerializedName("type")
    val characterType: Int?,
    @SerializedName("characterClass")
    val characterClass: Int?,
    @SerializedName("rank")
    val rank: Int?,
    @SerializedName("keyword")
    val spotType: String?,
    @SerializedName("spotName")
    val spotName: String?,
    @SerializedName("pic")
    val pic: String?,
    @SerializedName("reviewCd")
    val reviewCd: Boolean?,
    @SerializedName("review")
    val review: String?,
    @SerializedName("rating")
    val rating: Int?,

    ) {
    fun toDomain(): Review = Review(
        reviewId = reviewId.orZero(),
        spotId = spotId.orZero(),
        distance = distance.orZero(),
        step = step.orZero(),
        date = date.orEmpty(),
        startTime = startTime.orEmpty(),
        endTime = endTime.orEmpty(),
        characterId = characterId.orZero(),
        characterClass = characterClass.orZero(),
        characterType = characterType.orZero(),
        spotType = spotType.orEmpty(),
        spotName = spotName.orEmpty(),
        rank = rank.orZero(),
        pic = pic.orEmpty(),
        reviewCd = reviewCd.orFalse(),
        review = review.orEmpty(),
        rating = rating.orZero()
    )
}