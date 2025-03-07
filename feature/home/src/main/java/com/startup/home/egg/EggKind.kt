package com.startup.home.egg

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.startup.home.R
import com.startup.ui.WalkieTheme

enum class EggKind(@StringRes val displayStrResId: Int, @DrawableRes val drawableResId: Int) {
    Normal(R.string.clazz_normal, R.drawable.egg_1),
    Rare(R.string.clazz_rare, R.drawable.egg_2),
    Epic(R.string.clazz_epic, R.drawable.egg_3),
    Legend(R.string.clazz_legend, R.drawable.egg_4);

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
        fun rankOfEggKind(rank: Int): EggKind {
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