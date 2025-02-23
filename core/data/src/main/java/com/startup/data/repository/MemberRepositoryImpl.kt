package com.startup.data.repository

import com.startup.common.extension.orFalse
import com.startup.data.datasource.MemberDataSource
import com.startup.data.remote.dto.request.character.CharacterIdRequest
import com.startup.data.remote.dto.request.egg.WalkingEggRequest
import com.startup.data.remote.dto.request.member.MemberNickNameRequest
import com.startup.domain.model.character.MyCharacterWithWalk
import com.startup.domain.model.egg.MyEgg
import com.startup.domain.model.member.UserInfo
import com.startup.domain.repository.MemberRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class MemberRepositoryImpl @Inject constructor(private val memberDataSource: MemberDataSource): MemberRepository {
    override fun getUserInfo(): Flow<UserInfo> = memberDataSource.getUserInfo().map { it.toDomain() }

    override fun modifyUserInfo(memberNickname: String): Flow<Unit> = memberDataSource.modifyUserInfo(
        MemberNickNameRequest(memberNickname)
    )

    override fun updateWalkingEgg(eggId: Long): Flow<Unit> = memberDataSource.updateWalkingEgg(
        WalkingEggRequest(eggId)
    )

    override fun getWalkingEgg(): Flow<MyEgg> = memberDataSource.getWalkingEgg().map { it.toDomain() }

    override fun modifyWalkingCharacter(characterId: Long): Flow<Unit> = memberDataSource.modifyWalkingCharacter(
        CharacterIdRequest(characterId)
    )

    override fun getWalkingCharacter(): Flow<MyCharacterWithWalk> = memberDataSource.getWalkingCharacter().map { it.toDomain() }

    override fun changeUserProfileVisibility(): Flow<Boolean> = memberDataSource.changeUserProfileVisibility().map { it.isPublic.orFalse() }
}