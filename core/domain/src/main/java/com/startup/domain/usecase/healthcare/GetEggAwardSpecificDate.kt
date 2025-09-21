package com.startup.domain.usecase.healthcare

import com.startup.domain.model.egg.GetEggAWard
import com.startup.domain.repository.HealthcareRepository
import com.startup.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEggAwardSpecificDate @Inject constructor(private val healthcareRepository: HealthcareRepository) :
    BaseUseCase<Unit, GetEggAWard>() {
    override fun invoke(params: GetEggAWard): Flow<Unit> = healthcareRepository.postEggGet(params)
}