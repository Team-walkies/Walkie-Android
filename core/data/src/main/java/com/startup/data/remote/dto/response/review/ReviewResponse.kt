package com.startup.data.remote.dto.response.review

import com.google.gson.annotations.SerializedName

data class ReviewResponse(
    @SerializedName("reviews")
    val reviews: List<ReviewDto>?
)