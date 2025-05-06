package com.startup.data.repository

import com.startup.common.extension.orZero
import com.startup.data.datasource.ReviewDataSource
import com.startup.data.remote.dto.request.review.ModifyReviewRequest
import com.startup.domain.model.review.ModifyReview
import com.startup.domain.model.review.Review
import com.startup.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(private val reviewDataSource: ReviewDataSource) : ReviewRepository {
    override fun modifyReview(reviewId: Int, request: ModifyReview): Flow<Unit> =
        reviewDataSource.modifyReview(reviewId, ModifyReviewRequest(review = request.review, rating = request.rating))

    override fun deleteReview(reviewId: Int): Flow<Int> = reviewDataSource.deleteReview(reviewId)

    override fun getSpotOfReviews(spotId: Long): Flow<Int> =
        reviewDataSource.getSpotOfReviews(spotId).map { it.count.orZero() }

    override fun getCalendarReviewList(startDate: String, endDate: String): Flow<List<Review>> = reviewDataSource
        .getCalendarReviewList(startDate, endDate)
        .map { response -> response.reviews?.map { it.toDomain() } ?: emptyList() }

    override fun getSpotOfReviewList(spotId: Long): Flow<List<Review>> = reviewDataSource
        .getSpotOfReviewList(spotId)
        .map { response -> response.reviews?.map { it.toDomain() } ?: emptyList() }
}