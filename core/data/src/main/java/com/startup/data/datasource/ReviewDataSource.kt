package com.startup.data.datasource

import com.startup.data.remote.dto.request.review.ModifyReviewRequest
import com.startup.data.remote.dto.response.review.ReviewResponse
import com.startup.data.remote.dto.response.review.SpotCountResponse
import com.startup.data.remote.dto.request.review.WriteReviewRequest
import kotlinx.coroutines.flow.Flow

interface ReviewDataSource {
    fun writeReview(request: WriteReviewRequest): Flow<Unit>
    fun modifyReview(reviewId: Long, request: ModifyReviewRequest): Flow<Unit>
    fun deleteReview(reviewId: Long): Flow<Unit>
    fun getSpotOfReviews(spotId: Long): Flow<SpotCountResponse>
    fun getCalendarReviewList(date: String): Flow<ReviewResponse>
    fun getSpotOfReviewList(spotId: Long): Flow<ReviewResponse>
}