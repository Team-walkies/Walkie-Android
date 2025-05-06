package com.startup.data.datasource

import com.startup.data.remote.dto.request.review.ModifyReviewRequest
import com.startup.data.remote.dto.response.review.ReviewResponse
import com.startup.data.remote.dto.response.review.SpotCountResponse
import kotlinx.coroutines.flow.Flow

interface ReviewDataSource {
    fun modifyReview(reviewId: Int, request: ModifyReviewRequest): Flow<Unit>
    fun deleteReview(reviewId: Int): Flow<Int>
    fun getSpotOfReviews(spotId: Long): Flow<SpotCountResponse>
    fun getCalendarReviewList(startDate: String, endDate: String): Flow<ReviewResponse>
    fun getSpotOfReviewList(spotId: Long): Flow<ReviewResponse>
}