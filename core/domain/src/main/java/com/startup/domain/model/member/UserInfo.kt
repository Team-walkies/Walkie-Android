package com.startup.domain.model.member

data class UserInfo(
    val memberId: Long,
    val memberEmail: String,
    val memberNickName: String,
    /** 멤버가 탐험한 스팟 수 */
    val exploredSpotCount: Int,
    /** 멤버 프로필 공개 여부 */
    val isPublic: Boolean,
    /** 멤버 티어, 일단 초보 워키 하나만 */
    val memberTier: String
)
