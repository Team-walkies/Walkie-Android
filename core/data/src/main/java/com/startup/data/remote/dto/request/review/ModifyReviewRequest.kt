package com.startup.data.remote.dto.request.review

import com.google.gson.annotations.SerializedName

data class ModifyReviewRequest(
    @SerializedName("review")
    val review: String,
    @SerializedName("rating")
    val rating: Int
)
