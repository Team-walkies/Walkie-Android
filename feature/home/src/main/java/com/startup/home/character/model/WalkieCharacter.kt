package com.startup.home.character.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.startup.domain.model.character.MyCharacter
import com.startup.domain.model.character.MyCharacterWithWalk
import com.startup.home.R
import com.startup.home.character.CharacterRankKind

data class WalkieCharacter(
    val characterId: Long,
    val characterKind: CharacterKind,
    @DrawableRes val characterImageResId: Int,
    @StringRes val characterNameResId: Int,
) {
    companion object {
        fun MyCharacterWithWalk.toUiModel(): WalkieCharacter {
            return getTypeAndClassOfWalkieCharacter(characterId, type, clazz, rank)
        }

        fun MyCharacter.toUiModel() : WalkieCharacter {
            return getTypeAndClassOfWalkieCharacter(characterId, type, clazz, rank)
        }
        fun ofEmpty(): WalkieCharacter = WalkieCharacter(
            characterId = -1,
            characterNameResId = R.string.jellyfish_basic,
            characterImageResId = R.drawable.jelly_1,
            characterKind = CharacterKind.Jellyfish
        )

        fun getTypeAndClassOfWalkieCharacter(
            characterId: Long,
            type: Int,
            clazz: Int,
            rank: Int,
        ): WalkieCharacter {

            val characterKind = if (type == 1) {
                CharacterKind.Dino
            } else {
                CharacterKind.Jellyfish
            }
            val characterRankKind = CharacterRankKind.rankOfCharacterRankKind(rank)
            val characterData =
                characterRankKind.getCharacterNameAndImageWithTypeOfClass(characterKind, clazz)
            return WalkieCharacter(
                characterId = characterId,
                characterKind = characterKind,
                characterImageResId = characterData.imageResource,
                characterNameResId = characterData.nameResId
            )
        }
    }
}
