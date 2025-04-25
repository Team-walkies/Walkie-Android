package com.startup.domain.model.spot

data class ModifyReviewWebPostRequest(val accessToken: String, val reviewId: Int, val spotId: Int)