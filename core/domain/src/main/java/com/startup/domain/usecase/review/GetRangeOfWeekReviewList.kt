package com.startup.domain.usecase.review

import com.startup.domain.model.review.Review
import com.startup.domain.repository.ReviewRepository
import com.startup.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRangeOfWeekReviewList @Inject constructor(private val reviewRepository: ReviewRepository) :
    BaseUseCase<List<Review>, Pair<String, String>>() {
    override fun invoke(params: Pair<String, String>): Flow<List<Review>> =
        reviewRepository.getCalendarReviewList(params.first, params.second)
}