package com.startup.domain.usecase.profile

import com.startup.domain.repository.MemberRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateNickname @Inject constructor(
    private val memberRepository: MemberRepository
) {
    operator fun invoke(nickname: String): Flow<Unit> = memberRepository.modifyUserInfo(nickname)
}