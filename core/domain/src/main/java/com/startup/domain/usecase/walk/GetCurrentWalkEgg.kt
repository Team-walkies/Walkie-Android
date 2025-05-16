package com.startup.domain.usecase.walk

import com.startup.domain.model.egg.MyEgg
import com.startup.domain.repository.MemberRepository
import com.startup.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentWalkEgg @Inject constructor(private val memberRepository: MemberRepository): BaseUseCase<MyEgg, Unit>() {
    override fun invoke(params: Unit): Flow<MyEgg> = memberRepository.getWalkingEgg()
}