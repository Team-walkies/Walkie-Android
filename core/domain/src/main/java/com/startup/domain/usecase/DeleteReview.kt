package com.startup.domain.usecase

import com.startup.domain.repository.ReviewRepository
import com.startup.domain.util.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteReview @Inject constructor(private val reviewRepository: ReviewRepository) :
    BaseUseCase<Int, Int>() {
    override fun invoke(params: Int): Flow<Int> =
        reviewRepository.deleteReview(params)
}