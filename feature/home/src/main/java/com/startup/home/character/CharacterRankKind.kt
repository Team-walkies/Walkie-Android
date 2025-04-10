package com.startup.home.character

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.startup.home.R
import com.startup.home.character.model.CharacterKind
import com.startup.home.character.model.WalkieCharacterData
import com.startup.home.egg.model.EggKind
import com.startup.home.egg.model.EggKind.Empty
import com.startup.home.egg.model.EggKind.Epic
import com.startup.home.egg.model.EggKind.Legend
import com.startup.home.egg.model.EggKind.Normal
import com.startup.home.egg.model.EggKind.Rare
import com.startup.ui.WalkieTheme

enum class CharacterRankKind(
    @StringRes val displayStrResId: Int,
    @StringRes val characterStrResId: Int = R.string.character
) {
    Normal(R.string.clazz_normal),
    Rare(R.string.clazz_rare),
    Epic(R.string.clazz_epic),
    Legend(R.string.clazz_legend);

    fun getCharacterNameAndImageWithTypeOfClass(
        type: CharacterKind,
        clazz: Int
    ): WalkieCharacterData {
        return when (type) {
            CharacterKind.Jellyfish -> {
                when (this) {
                    Normal -> {
                        when (clazz) {
                            0 -> WalkieCharacterData(R.string.jellyfish_basic, R.drawable.jelly_1)
                            1 -> WalkieCharacterData(R.string.jellyfish_red, R.drawable.jelly_2)
                            2 -> WalkieCharacterData(R.string.jellyfish_green, R.drawable.jelly_3)
                            3 -> WalkieCharacterData(R.string.jellyfish_purple, R.drawable.jelly_4)
                            else -> WalkieCharacterData(R.string.jellyfish_pink, R.drawable.jelly_5) // 4
                        }
                    }

                    Rare -> {
                        when (clazz) {
                            0 -> WalkieCharacterData(R.string.jellyfish_rabbit, R.drawable.jelly_6)
                            else -> WalkieCharacterData(R.string.jellyfish_starfish, R.drawable.jelly_7) // 1
                        }
                    }

                    Epic -> {
                        when (clazz) {
                            0 -> WalkieCharacterData(R.string.jellyfish_electric_shock, R.drawable.jelly_8)
                            else -> WalkieCharacterData(R.string.jellyfish_strawberry_ricecake, R.drawable.jelly_9) //1
                        }
                    }

                    Legend -> WalkieCharacterData(
                        R.string.jellyfish_space,
                        R.drawable.jelly_10
                    ) // 0
                }
            }

            CharacterKind.Dino -> {
                when (this) {
                    Normal -> {
                        when (clazz) {
                            0 -> WalkieCharacterData(R.string.dino_basic, R.drawable.dino_1)
                            1 -> WalkieCharacterData(R.string.dino_red, R.drawable.dino_2)
                            2 -> WalkieCharacterData(R.string.dino_mint, R.drawable.dino_3)
                            3 -> WalkieCharacterData(R.string.dino_purple, R.drawable.dino_4)
                            else -> WalkieCharacterData(R.string.dino_pink, R.drawable.dino_5) // 4
                        }
                    }

                    Rare -> {
                        when (clazz) {
                            0 -> WalkieCharacterData(R.string.dino_reindeer, R.drawable.dino_6)
                            else -> WalkieCharacterData(R.string.dino_ness, R.drawable.dino_7) // 1
                        }
                    }

                    Epic -> {
                        when (clazz) {
                            0 -> WalkieCharacterData(R.string.dino_pancake, R.drawable.dino_8)
                            else -> WalkieCharacterData(R.string.dino_melon_soda, R.drawable.dino_9) //1
                        }
                    }

                    Legend -> WalkieCharacterData(R.string.dino_dragon, R.drawable.dino_10) // 0
                }

            }
        }
    }

    @Composable
    fun getTextColor(): Color {
        return when (this) {
            Normal -> {
                WalkieTheme.colors.blue500
            }

            Rare -> {
                WalkieTheme.colors.green300
            }

            Epic -> {
                WalkieTheme.colors.orange300
            }

            Legend -> {
                WalkieTheme.colors.purple300
            }
        }
    }

    companion object {
        fun rankOfCharacterRankKind(rank: Int): CharacterRankKind {
            return when (rank) {
                0 -> Normal
                1 -> Rare
                2 -> Epic
                3 -> Legend
                else -> Normal
            }
        }
    }
}
