package com.startup.domain.usecase.spot

import com.startup.domain.model.spot.SpotWebPostRequest
import com.startup.domain.repository.AuthRepository
import com.startup.domain.repository.MemberRepository
import com.startup.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetSpotWebViewParams @Inject constructor(
    private val authRepository: AuthRepository,
    private val memberRepository: MemberRepository
) : BaseUseCase<SpotWebPostRequest, Unit>() {
    override fun invoke(params: Unit): Flow<SpotWebPostRequest> = combine(
        authRepository.getAccessToken(),
        memberRepository.getWalkingCharacter()
    ) { token, character ->
        SpotWebPostRequest(
            accessToken = token,
            characterType = character.type,
            characterClass = character.clazz,
            characterRank = character.rank,
            characterId = character.characterId
        )
    }
}