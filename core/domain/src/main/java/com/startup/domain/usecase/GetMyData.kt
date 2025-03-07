package com.startup.domain.usecase

import com.startup.domain.model.member.UserInfo
import com.startup.domain.repository.MemberRepository
import com.startup.domain.util.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMyData @Inject constructor(private val memberRepository: MemberRepository) : BaseUseCase<UserInfo, Unit>() {
    override fun invoke(params: Unit): Flow<UserInfo> = memberRepository.getUserInfo()
}