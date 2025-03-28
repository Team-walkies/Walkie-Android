package com.startup.domain.usecase

import com.startup.domain.model.spot.SpotWebPostRequest
import com.startup.domain.repository.AuthRepository
import com.startup.domain.repository.MemberRepository
import com.startup.domain.util.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetSpotWebViewParams @Inject constructor(
    private val authRepository: AuthRepository,
    private val memberRepository: MemberRepository
) : BaseUseCase<SpotWebPostRequest, Unit>() {
    override fun invoke(params: Unit): Flow<SpotWebPostRequest> = combine(
        authRepository.getAccessToken(),
        memberRepository.getUserInfo(),
        memberRepository.getWalkingCharacter()
    ) { token, userInfo, character ->
        SpotWebPostRequest(
            accessToken = token,
            characterType = character.type,
            characterClass = character.clazz,
            characterRank = character.rank,
            userId = userInfo.memberId
        )
    }
}