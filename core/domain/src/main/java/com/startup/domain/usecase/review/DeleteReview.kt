package com.startup.domain.usecase.review

import com.startup.domain.repository.ReviewRepository
import com.startup.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteReview @Inject constructor(private val reviewRepository: ReviewRepository) :
    BaseUseCase<Int, Int>() {
    override fun invoke(params: Int): Flow<Int> =
        reviewRepository.deleteReview(params)
}