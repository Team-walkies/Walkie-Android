package com.startup.domain.usecase.spot

import com.startup.domain.repository.MemberRepository
import com.startup.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecordedSpotCount @Inject constructor(
    private val memberRepository: MemberRepository
) : BaseUseCase<Int, Unit>() {
    override fun invoke(params: Unit): Flow<Int> = memberRepository.getRecordedSpotCount()
}