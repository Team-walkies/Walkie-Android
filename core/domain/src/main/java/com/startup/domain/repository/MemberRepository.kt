package com.startup.domain.repository

import com.startup.domain.model.character.MyCharacterWithWalk
import com.startup.domain.model.egg.MyEgg
import com.startup.domain.model.member.UserInfo
import kotlinx.coroutines.flow.Flow

interface MemberRepository {
    fun getUserInfo(): Flow<UserInfo>
    fun modifyUserInfo(memberNickname: String): Flow<Unit>
    fun updateWalkingEgg(eggId: Long): Flow<Unit>
    fun getWalkingEgg(): Flow<MyEgg>
    fun modifyWalkingCharacter(characterId: Long): Flow<Unit>
    fun getWalkingCharacter(): Flow<MyCharacterWithWalk>
    fun changeUserProfileVisibility(): Flow<Boolean>
}