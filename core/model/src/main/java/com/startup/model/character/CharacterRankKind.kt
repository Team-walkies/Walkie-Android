package com.startup.model.character

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.startup.design_system.ui.WalkieTheme
import com.startup.model.R

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
    ): Pair<Int, Int> {
        return when (type) {
            CharacterKind.Jellyfish -> {
                when (this) {
                    Normal -> {
                        when (clazz) {
                            0 -> R.string.jellyfish_basic to R.drawable.jelly_1
                            1 -> R.string.jellyfish_red to R.drawable.jelly_2
                            2 -> R.string.jellyfish_green to R.drawable.jelly_3
                            3 -> R.string.jellyfish_purple to R.drawable.jelly_4
                            else -> R.string.jellyfish_pink to R.drawable.jelly_5 // 4
                        }
                    }

                    Rare -> {
                        when (clazz) {
                            0 -> R.string.jellyfish_rabbit to R.drawable.jelly_6
                            else -> R.string.jellyfish_starfish to R.drawable.jelly_7 // 1
                        }
                    }

                    Epic -> {
                        when (clazz) {
                            0 -> R.string.jellyfish_electric_shock to R.drawable.jelly_8
                            else -> R.string.jellyfish_strawberry_ricecake to R.drawable.jelly_9 //1
                        }
                    }

                    Legend -> R.string.jellyfish_space to R.drawable.jelly_10  // 0
                }
            }

            CharacterKind.Dino -> {
                when (this) {
                    Normal -> {
                        when (clazz) {
                            0 -> R.string.dino_basic to R.drawable.dino_1
                            1 -> R.string.dino_red to R.drawable.dino_2
                            2 -> R.string.dino_mint to R.drawable.dino_3
                            3 -> R.string.dino_purple to R.drawable.dino_4
                            else -> R.string.dino_pink to R.drawable.dino_5 // 4
                        }
                    }

                    Rare -> {
                        when (clazz) {
                            0 -> R.string.dino_reindeer to R.drawable.dino_6
                            else -> R.string.dino_ness to R.drawable.dino_7 // 1
                        }
                    }

                    Epic -> {
                        when (clazz) {
                            0 -> R.string.dino_pancake to R.drawable.dino_8
                            else -> R.string.dino_melon_soda to R.drawable.dino_9 //1
                        }
                    }

                    Legend -> R.string.dino_dragon to R.drawable.dino_10 // 0
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
