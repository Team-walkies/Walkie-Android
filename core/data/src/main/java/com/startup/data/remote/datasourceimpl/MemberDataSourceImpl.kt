package com.startup.data.remote.datasourceimpl

import com.startup.data.datasource.MemberDataSource
import com.startup.data.remote.dto.response.character.CharacterDto
import com.startup.data.remote.dto.request.character.CharacterIdRequest
import com.startup.data.remote.dto.response.egg.EggDto
import com.startup.data.remote.dto.request.egg.WalkingEggRequest
import com.startup.data.remote.dto.request.member.MemberNickNameRequest
import com.startup.data.remote.dto.response.member.GetUserInfoResponse
import com.startup.data.remote.dto.response.member.IsPublicDto
import com.startup.data.remote.ext.emitRemote
import com.startup.data.remote.service.MemberService
import com.startup.data.util.handleExceptionIfNeed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class MemberDataSourceImpl @Inject constructor(private val memberService: MemberService) :
    MemberDataSource {
    override fun getUserInfo(): Flow<GetUserInfoResponse> = flow {
        handleExceptionIfNeed {
            emitRemote(memberService.getUserInfo(), specificErrorCode = 204)
        }
    }

    override fun modifyUserInfo(request: MemberNickNameRequest): Flow<Unit> = flow {
        handleExceptionIfNeed {
            emitRemote(memberService.modifyUserInfo(request))
        }
    }

    override fun updateWalkingEgg(request: WalkingEggRequest): Flow<Unit> = flow {
        handleExceptionIfNeed {
            emitRemote(memberService.updateWalkingEgg(request))
        }
    }

    override fun getWalkingEgg(): Flow<EggDto> = flow {
        handleExceptionIfNeed {
            emitRemote(memberService.getWalkingEgg())
        }
    }

    override fun modifyWalkingCharacter(request: CharacterIdRequest): Flow<Unit> = flow {
        handleExceptionIfNeed {
            emitRemote(memberService.modifyWalkingCharacter(request))
        }
    }

    override fun getWalkingCharacter(): Flow<CharacterDto> = flow {
        handleExceptionIfNeed {
            emitRemote(memberService.getWalkingCharacter())
        }
    }

    override fun changeUserProfileVisibility(): Flow<IsPublicDto> = flow {
        handleExceptionIfNeed {
            emitRemote(memberService.changeUserProfileVisibility())
        }
    }

    override fun getRecordedSpotCount(): Flow<Int> = flow {
        handleExceptionIfNeed {
            emitRemote(memberService.getRecordedSpotCount())
        }
    }
}