package com.startup.data.remote.dto.request.review

import com.google.gson.annotations.SerializedName

/** 리뷰 작성 API */
data class WriteReviewRequest(
    @SerializedName("spotId")
    val spotId: Int,
    @SerializedName("distance")
    val distance: Double,
    @SerializedName("step")
    val step: Int,
    @SerializedName("date")
    val date: String, // ex yyyy-MM-dd
    @SerializedName("startTime")
    val startTime: String, // hh:mm:ss
    @SerializedName("endTime")
    val endTime: String, // hh:mm:ss
    @SerializedName("characterId")
    val characterId: Long,
    @SerializedName("pic")
    val pic: String, // 대표 사진 Url
    @SerializedName("reviewCd")
    val reviewCd: Boolean,
    @SerializedName("review")
    val review: String,
    @SerializedName("rating")
    val rating: Int,
)