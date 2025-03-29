package com.startup.domain.usecase

import com.startup.domain.model.egg.MyEgg
import com.startup.domain.repository.MemberRepository
import com.startup.domain.util.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentWalkEgg @Inject constructor(private val memberRepository: MemberRepository): BaseUseCase<MyEgg, Unit>() {
    override fun invoke(params: Unit): Flow<MyEgg> = memberRepository.getWalkingEgg()
}