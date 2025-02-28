package com.startup.data.remote.dto.response.member

import com.google.gson.annotations.SerializedName
import com.startup.common.extension.orFalse
import com.startup.common.extension.orZero
import com.startup.domain.model.member.UserInfo

data class GetUserInfoResponse(
    @SerializedName("memberId")
    val memberId: Long?,
    @SerializedName("memberEmail")
    val memberEmail: String?,
    @SerializedName("memberNickName")
    val memberNickName: String?,
    @SerializedName("exploredSpotCount")
    val exploredSpotCount: Int?,
    @SerializedName("isPublic")
    val isPublic: Boolean?,
    @SerializedName("memberTier")
    val memberTier: String?
) {
    fun toDomain(): UserInfo = UserInfo(
        memberId = memberId.orZero(),
        memberEmail = memberEmail.orEmpty(),
        memberNickName = memberNickName.orEmpty(),
        exploredSpotCount = exploredSpotCount.orZero(),
        isPublic = isPublic.orFalse(),
        memberTier = memberTier.orEmpty()
    )
}