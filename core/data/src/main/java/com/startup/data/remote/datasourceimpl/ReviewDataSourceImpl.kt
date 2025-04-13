package com.startup.data.remote.datasourceimpl

import com.startup.data.datasource.ReviewDataSource
import com.startup.data.remote.dto.request.review.ModifyReviewRequest
import com.startup.data.remote.dto.response.review.ReviewResponse
import com.startup.data.remote.dto.response.review.SpotCountResponse
import com.startup.data.remote.ext.emitRemote
import com.startup.data.remote.service.ReviewService
import com.startup.data.util.handleExceptionIfNeed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class ReviewDataSourceImpl @Inject constructor(private val reviewService: ReviewService) :
    ReviewDataSource {

    override fun modifyReview(reviewId: Int, request: ModifyReviewRequest): Flow<Unit> = flow {
        handleExceptionIfNeed {
            emitRemote(reviewService.modifyReview(reviewId, request))
        }
    }

    override fun deleteReview(reviewId: Int): Flow<Unit> = flow {
        handleExceptionIfNeed {
            emitRemote(reviewService.deleteReview(reviewId))
        }
    }

    override fun getSpotOfReviews(spotId: Long): Flow<SpotCountResponse> = flow {
        handleExceptionIfNeed {
            emitRemote(reviewService.getSpotOfReviews(spotId))
        }
    }

    override fun getCalendarReviewList(startDate: String, endDate: String): Flow<ReviewResponse> = flow {
        handleExceptionIfNeed {
            emitRemote(reviewService.getCalendarReviewList(startDate, endDate))
        }
    }

    override fun getSpotOfReviewList(spotId: Long): Flow<ReviewResponse> = flow {
        handleExceptionIfNeed {
            emitRemote(reviewService.getSpotOfReviewList(spotId))
        }
    }
}