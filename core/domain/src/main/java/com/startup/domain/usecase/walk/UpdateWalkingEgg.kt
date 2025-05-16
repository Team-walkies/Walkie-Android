package com.startup.domain.usecase.walk

import com.startup.domain.repository.MemberRepository
import com.startup.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateWalkingEgg @Inject constructor(private val memberRepository: MemberRepository) :
    BaseUseCase<Unit, Long>() {
    override fun invoke(params: Long): Flow<Unit> = memberRepository.updateWalkingEgg(params)
}