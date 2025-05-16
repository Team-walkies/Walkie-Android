package com.startup.domain.model.member

data class UserInfo(
    val memberId: Long,
    val memberEmail: String,
    val memberNickName: String,
    /** 멤버 프로필 공개 여부 */
    val isPublic: Boolean,
    /** 멤버 티어, 일단 초보 워키 하나만 */
    val memberTier: String,
    val providerId: String,
    val provider: String,
    /** 멤버가 탐험한 스팟 수 */
    val exploredSpot: Int,
    /** 멤버가 기록한 스팟 수 */
    val recordedSpot: Int,
    val userCharacterId: Long,
    val eggId: Long
) {
    companion object {
        fun ofEmpty(): UserInfo = UserInfo(
            memberId = -1,
            memberEmail = "",
            memberNickName = "",
            isPublic = false,
            memberTier = "",
            providerId = "",
            provider = "",
            exploredSpot = 0,
            recordedSpot = 0,
            userCharacterId = -1,
            eggId = -1
        )
    }
}
