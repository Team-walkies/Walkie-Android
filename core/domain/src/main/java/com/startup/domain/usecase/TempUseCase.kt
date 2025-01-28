package com.startup.domain.usecase

import com.startup.domain.repository.TempRepository
import com.startup.domain.util.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TempUseCase @Inject constructor(private val tempRepository: TempRepository) : BaseUseCase<Unit, Unit>() {
    override fun invoke(params: Unit): Flow<Unit> {
        return tempRepository.getData()
    }
}