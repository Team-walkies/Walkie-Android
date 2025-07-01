package com.startup.model.character

import com.startup.domain.model.character.CharacterDetail
import com.startup.domain.model.character.CharacterObtainInfo

data class WalkieCharacterDetail(
    val characterId: Long,
    val characterKind: CharacterKind,
    val rank: CharacterRankKind,
    val characterImageUrl: String,
    val characterName: String,
    val picked: Boolean,
    val count: Int,
    val obtainInfoList: List<CharacterObtainInfo>
) {
    companion object {
        fun CharacterDetail.toUiModel(): WalkieCharacterDetail {
            val characterRankKind = CharacterRankKind.rankOfCharacterRankKind(rank)

            val characterKind = if (type == 1) {
                CharacterKind.Dino
            } else {
                CharacterKind.Jellyfish
            }
            return WalkieCharacterDetail(
                characterId = characterId,
                characterKind = characterKind,
                characterImageUrl = characterImageUrl,
                characterName = characterName,
                rank = characterRankKind,
                picked = false,
                count = characterCount,
                obtainInfoList = obtainInfo
            )
        }
    }
}