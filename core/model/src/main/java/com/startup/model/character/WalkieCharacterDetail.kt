package com.startup.model.character

import com.startup.domain.model.character.CharacterDetail
import com.startup.domain.model.character.CharacterObtainInfo

data class WalkieCharacterDetail(val character: WalkieCharacter, val obtainInfoList: List<CharacterObtainInfo>) {
    companion object {
        fun CharacterDetail.toUiModel(): WalkieCharacterDetail {
            return WalkieCharacterDetail(
                character = WalkieCharacter.getTypeAndClassOfWalkieCharacter(
                    characterId = characterId,
                    type = type,
                    clazz = characterClass,
                    count = characterCount,
                    rank = rank,
                    picked = false
                ),
                obtainInfoList = obtainInfo
            )
        }
    }
}