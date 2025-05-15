package com.startup.model.character

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.startup.domain.model.character.MyCharacter
import com.startup.domain.model.character.MyCharacterWithWalk
import com.startup.model.R

data class WalkieCharacter(
    val characterId: Long,
    val characterKind: CharacterKind,
    val rank: CharacterRankKind,
    @DrawableRes val characterImageResId: Int,
    @StringRes val characterNameResId: Int,
    val picked: Boolean,
    val count: Int
) {
    fun isHatched(): Boolean = count > 0

    companion object {

        fun MyCharacterWithWalk.toUiModel(): WalkieCharacter {
            return getTypeAndClassOfWalkieCharacter(characterId, type, clazz, rank, picked, count)
        }

        fun MyCharacter.toUiModel(): WalkieCharacter {
            return getTypeAndClassOfWalkieCharacter(characterId, type, characterClass, rank, picked, count)
        }

        fun List<MyCharacter>.toUiModel(): List<WalkieCharacter> {
            return map {
                it.toUiModel()
            }
        }

        fun ofEmpty(): WalkieCharacter = WalkieCharacter(
            characterId = -1,
            characterNameResId = R.string.jellyfish_basic,
            characterImageResId = R.drawable.jelly_1,
            characterKind = CharacterKind.Jellyfish,
            rank = CharacterRankKind.Normal,
            picked = false,
            count = 0
        )

        fun getTypeAndClassOfWalkieCharacter(
            characterId: Long,
            type: Int,
            clazz: Int,
            rank: Int,
            picked: Boolean,
            count: Int
        ): WalkieCharacter {

            val characterKind = if (type == 1) {
                CharacterKind.Dino
            } else {
                CharacterKind.Jellyfish
            }
            val characterRankKind = CharacterRankKind.rankOfCharacterRankKind(rank)
            val (imageResource, nameResId) = characterRankKind.getCharacterNameAndImageWithTypeOfClass(characterKind, clazz)
            return WalkieCharacter(
                characterId = characterId,
                characterKind = characterKind,
                characterImageResId = imageResource,
                characterNameResId = nameResId,
                rank = characterRankKind,
                picked = picked,
                count = count
            )
        }
    }
}
