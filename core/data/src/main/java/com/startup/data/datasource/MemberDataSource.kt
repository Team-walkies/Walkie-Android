package com.startup.data.datasource

import com.startup.data.remote.dto.request.character.CharacterIdRequest
import com.startup.data.remote.dto.request.egg.WalkingEggRequest
import com.startup.data.remote.dto.request.member.MemberNickNameRequest
import com.startup.data.remote.dto.response.character.CharacterDto
import com.startup.data.remote.dto.response.egg.EggDto
import com.startup.data.remote.dto.response.member.GetUserInfoResponse
import com.startup.data.remote.dto.response.member.IsPublicDto
import kotlinx.coroutines.flow.Flow

interface MemberDataSource {
    fun getUserInfo(): Flow<GetUserInfoResponse>
    fun modifyUserInfo(request: MemberNickNameRequest): Flow<Unit>
    fun updateWalkingEgg(request: WalkingEggRequest): Flow<Unit>
    fun getWalkingEgg(): Flow<EggDto>
    fun modifyWalkingCharacter(request: CharacterIdRequest): Flow<Unit>
    fun getWalkingCharacter(): Flow<CharacterDto>
    fun changeUserProfileVisibility(): Flow<IsPublicDto>
    fun getRecordedSpotCount(): Flow<Int>
}