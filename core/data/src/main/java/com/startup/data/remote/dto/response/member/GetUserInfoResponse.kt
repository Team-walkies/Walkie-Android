package com.startup.data.remote.dto.response.member

import com.google.gson.annotations.SerializedName
import com.startup.common.extension.orFalse
import com.startup.common.extension.orZero
import com.startup.domain.model.member.UserInfo

data class GetUserInfoResponse(
    @SerializedName("id")
    val memberId: Long?,
    @SerializedName("memberEmail")
    val memberEmail: String?,
    @SerializedName("nickname")
    val nickname: String?,
    @SerializedName("isPublic")
    val isPublic: Boolean?,
    @SerializedName("memberTier")
    val memberTier: String?,
    @SerializedName("providerId")
    val providerId: String?,
    @SerializedName("provider")
    val provider: String?,
    @SerializedName("exploredSpot")
    val exploredSpot: Int?,
    @SerializedName("recordedSpot")
    val recordedSpot: Int?,
    @SerializedName("userCharacterId")
    val userCharacterId: Long?,
    @SerializedName("eggId")
    val eggId: Long?
) {
    fun toDomain(): UserInfo = UserInfo(
        memberId = memberId.orZero(),
        memberEmail = memberEmail.orEmpty(),
        memberNickName = nickname.orEmpty(),
        isPublic = isPublic.orFalse(),
        memberTier = memberTier.orEmpty(),
        providerId = providerId.orEmpty(),
        provider = provider.orEmpty(),
        exploredSpot = exploredSpot.orZero(),
        recordedSpot = recordedSpot.orZero(),
        userCharacterId = userCharacterId.orZero(),
        eggId = eggId.orZero()
    )
}