package com.startup.domain.repository

import com.startup.domain.model.review.ModifyReview
import com.startup.domain.model.review.Review
import kotlinx.coroutines.flow.Flow

interface ReviewRepository {
    fun modifyReview(reviewId: Int, request: ModifyReview): Flow<Unit>
    fun deleteReview(reviewId: Int): Flow<Unit>
    fun getSpotOfReviews(spotId: Long): Flow<Int>
    fun getCalendarReviewList(startDate: String, endDate: String): Flow<List<Review>>
    fun getSpotOfReviewList(spotId: Long): Flow<List<Review>>
}