package com.startup.home.character.model

import com.startup.home.R
import com.startup.home.egg.model.EggKind

data class WalkieCharacterData(
    val nameResId: Int,
    val imageResource: Int
)

interface BaseWalkieCharacter {
    val id: String
    val nameResId: Int
    val imageResource: Int
    val rarity: EggKind // 나중에 필요하다면, 별도 분리

    fun getDescription(): Int
}

data class Jellyfish(
    override val id: String,
    override val nameResId: Int,
    override val rarity: EggKind,
    override val imageResource: Int
) : BaseWalkieCharacter {

    override fun getDescription(): Int {
        return R.string.character_jellyfish_description
    }
}

data class Dino(
    override val id: String,
    override val nameResId: Int,
    override val rarity: EggKind,
    override val imageResource: Int
) : BaseWalkieCharacter {

    override fun getDescription(): Int {
        return R.string.character_dino_description
    }
}


object CharacterFactory {
    fun getJellyfishCharacters(): List<Jellyfish> {
        return listOf(
            Jellyfish(
                "jellyfish_basic",
                R.string.jellyfish_basic,
                EggKind.Normal,
                R.drawable.jelly_1
            ),
            Jellyfish("jellyfish_red", R.string.jellyfish_red, EggKind.Normal, R.drawable.jelly_2),
            Jellyfish(
                "jellyfish_green",
                R.string.jellyfish_green,
                EggKind.Normal,
                R.drawable.jelly_3
            ),
            Jellyfish(
                "jellyfish_purple",
                R.string.jellyfish_purple,
                EggKind.Normal,
                R.drawable.jelly_4
            ),
            Jellyfish(
                "jellyfish_pink",
                R.string.jellyfish_pink,
                EggKind.Normal,
                R.drawable.jelly_5
            ),
            Jellyfish(
                "jellyfish_rabbit",
                R.string.jellyfish_rabbit,
                EggKind.Rare,
                R.drawable.jelly_6
            ),
            Jellyfish(
                "jellyfish_starfish",
                R.string.jellyfish_starfish,
                EggKind.Rare,
                R.drawable.jelly_7
            ),
            Jellyfish(
                "jellyfish_electric_shock",
                R.string.jellyfish_electric_shock,
                EggKind.Epic,
                R.drawable.jelly_8
            ),
            Jellyfish(
                "jellyfish_strawberry_ricecake",
                R.string.jellyfish_strawberry_ricecake,
                EggKind.Epic,
                R.drawable.jelly_9
            ),
            Jellyfish(
                "jellyfish_space",
                R.string.jellyfish_space,
                EggKind.Legend,
                R.drawable.jelly_10
            )
        )
    }

    fun getDinoCharacters(): List<Dino> {
        return listOf(
            Dino("dino_basic", R.string.dino_basic, EggKind.Normal, R.drawable.dino_1),
            Dino("dino_red", R.string.dino_red, EggKind.Normal, R.drawable.dino_2),
            Dino("dino_mint", R.string.dino_mint, EggKind.Normal, R.drawable.dino_3),
            Dino("dino_purple", R.string.dino_purple, EggKind.Normal, R.drawable.dino_4),
            Dino("dino_pink", R.string.dino_pink, EggKind.Normal, R.drawable.dino_5),
            Dino("dino_reindeer", R.string.dino_reindeer, EggKind.Rare, R.drawable.dino_6),
            Dino("dino_ness", R.string.dino_ness, EggKind.Rare, R.drawable.dino_7),
            Dino("dino_pancake", R.string.dino_pancake, EggKind.Epic, R.drawable.dino_8),
            Dino("dino_melon_soda", R.string.dino_melon_soda, EggKind.Epic, R.drawable.dino_9),
            Dino("dino_dragon", R.string.dino_dragon, EggKind.Legend, R.drawable.dino_10)
        )
    }

    fun getAllCharacters(): List<BaseWalkieCharacter> {
        val allCharacters = mutableListOf<BaseWalkieCharacter>()
        allCharacters.addAll(getJellyfishCharacters())
        allCharacters.addAll(getDinoCharacters())
        return allCharacters
    }
}