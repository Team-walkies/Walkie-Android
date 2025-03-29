package com.startup.domain.usecase

import com.startup.domain.model.character.MyCharacterWithWalk
import com.startup.domain.repository.MemberRepository
import com.startup.domain.util.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentWalkCharacter @Inject constructor(private val memberRepository: MemberRepository): BaseUseCase<MyCharacterWithWalk, Unit>() {
    override fun invoke(params: Unit): Flow<MyCharacterWithWalk> = memberRepository.getWalkingCharacter()
}