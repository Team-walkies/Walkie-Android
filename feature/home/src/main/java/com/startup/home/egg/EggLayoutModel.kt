package com.startup.home.egg

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.startup.home.R
import com.startup.ui.WalkieTheme


data class EggLayoutModel(
    @ColorRes val startGradient: Color,
    @ColorRes val endGradient: Color,
    @DrawableRes val eggDrawable: Int,
    @DrawableRes val effectDrawable: Int? = null
)

enum class EggTier {
    EMPTY, COMMON, RARE, EPIC, LEGENDARY,
}

@Composable
fun getEggLayoutModel(tier: EggTier): EggLayoutModel {
    return when (tier) {
        EggTier.EMPTY -> EggLayoutModel(
            startGradient = WalkieTheme.colors.blue300,
            endGradient = WalkieTheme.colors.blue200,
            eggDrawable = R.drawable.img_empty_egg
        )

        EggTier.COMMON -> EggLayoutModel(
            startGradient = WalkieTheme.colors.blue300,
            endGradient = WalkieTheme.colors.blue200,
            eggDrawable = R.drawable.egg_1
        )

        EggTier.RARE -> EggLayoutModel(
            startGradient = WalkieTheme.colors.green100,
            endGradient = WalkieTheme.colors.green50,
            eggDrawable = R.drawable.egg_2,
            effectDrawable = R.drawable.img_effect_rare
        )

        EggTier.EPIC -> EggLayoutModel(
            startGradient = WalkieTheme.colors.orange100,
            endGradient = WalkieTheme.colors.orange100,
            eggDrawable = R.drawable.egg_3,
            effectDrawable = R.drawable.img_effect_epic
        )

        EggTier.LEGENDARY -> EggLayoutModel(
            startGradient = WalkieTheme.colors.purple200,
            endGradient = WalkieTheme.colors.purple100,
            eggDrawable = R.drawable.egg_4,
            effectDrawable = R.drawable.img_effect_legendary
        )
    }
}