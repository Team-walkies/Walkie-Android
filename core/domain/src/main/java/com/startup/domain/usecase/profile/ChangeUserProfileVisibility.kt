package com.startup.domain.usecase.profile

import com.startup.domain.repository.MemberRepository
import com.startup.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChangeUserProfileVisibility @Inject constructor(private val memberRepository: MemberRepository): BaseUseCase<Boolean, Unit>() {
    override fun invoke(params: Unit): Flow<Boolean> = memberRepository.changeUserProfileVisibility()
}