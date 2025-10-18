package com.startup.model.egg

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.startup.design_system.ui.WalkieTheme
import com.startup.model.R

enum class EggKind(
    @StringRes val rankStrResId: Int,
    @DrawableRes val drawableResId: Int,
    val gainProbability: Float,
    @StringRes val eggStrResId: Int = R.string.egg,
    @StringRes val eggInfoStrRedId: Int
) {
    Empty(R.string.desc_empty_egg, R.drawable.img_empty_egg, 0F, eggInfoStrRedId = -1),
    Normal(R.string.clazz_normal, R.drawable.egg_normal, 0.65F, eggInfoStrRedId = R.string.normal_egg_info),
    Rare(R.string.clazz_rare, R.drawable.egg_rare, 0.2F, eggInfoStrRedId = R.string.rare_egg_info),
    Epic(R.string.clazz_epic, R.drawable.egg_epic, 0.12F, eggInfoStrRedId = R.string.epic_egg_info),
    Legend(R.string.clazz_legend, R.drawable.egg_legend, 0.03F, eggInfoStrRedId = R.string.legend_egg_info);

    @Composable
    fun getTextColor(): Color {

        return when (this) {
            Empty -> {
                WalkieTheme.colors.blue300
            }

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

    fun kindOfCharacterGainProbability(): EggOfCharacterGainProbabilityModel {
        return when (this) {
            Empty -> {
                EggOfCharacterGainProbabilityModel(
                    eggKind = this,
                    normalProbability = 0F,
                    rareProbability = 0F,
                    epicProbability = 0F,
                    legendProbability = 0F
                )
            }

            Normal -> {
                EggOfCharacterGainProbabilityModel(
                    eggKind = this,
                    normalProbability = 0.80F,
                    rareProbability = 0.15F,
                    epicProbability = 0.04F,
                    legendProbability = 0.01F
                )
            }

            Rare -> {
                EggOfCharacterGainProbabilityModel(
                    eggKind = this,
                    normalProbability = 0.50F,
                    rareProbability = 0.35F,
                    epicProbability = 0.10F,
                    legendProbability = 0.05F
                )
            }

            Epic -> {
                EggOfCharacterGainProbabilityModel(
                    eggKind = this,
                    normalProbability = 0.20F,
                    rareProbability = 0.40F,
                    epicProbability = 0.30F,
                    legendProbability = 0.10F
                )
            }

            Legend -> {
                EggOfCharacterGainProbabilityModel(
                    eggKind = this,
                    normalProbability = 0.05F,
                    rareProbability = 0.25F,
                    epicProbability = 0.40F,
                    legendProbability = 0.30F
                )
            }
        }
    }
    fun kindOfRankId():Int = when (this) {
        Empty -> -1
        Normal -> 0
        Rare -> 1
        Epic -> 2
        Legend -> 3
    }
    companion object {

        fun rankOfEggKind(rank: Int): EggKind {
            return when (rank) {
                -1 -> Empty
                0 -> Normal
                1 -> Rare
                2 -> Epic
                3 -> Legend
                else -> Empty
            }
        }
    }
}