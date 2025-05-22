package com.startup.model.egg

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.startup.design_system.ui.WalkieTheme
import com.startup.model.R


data class EggLayoutModel(
    @ColorRes val startGradient: Color,
    @ColorRes val endGradient: Color,
    @DrawableRes val eggDrawable: Int,
    @DrawableRes val effectDrawable: Int? = null
)

@Composable
fun getEggLayoutModel(kind: EggKind): EggLayoutModel {
    return when (kind) {
        EggKind.Empty -> EggLayoutModel(
            startGradient = WalkieTheme.colors.blue300,
            endGradient = WalkieTheme.colors.blue200,
            eggDrawable = R.drawable.img_egg_empty_crop
        )

        EggKind.Normal -> EggLayoutModel(
            startGradient = WalkieTheme.colors.blue300,
            endGradient = WalkieTheme.colors.blue200,
            eggDrawable = R.drawable.img_egg_normal_crop
        )

        EggKind.Rare -> EggLayoutModel(
            startGradient = WalkieTheme.colors.green100,
            endGradient = WalkieTheme.colors.green50,
            eggDrawable = R.drawable.img_egg_rare_crop,
            effectDrawable = R.drawable.img_effect_rare
        )

        EggKind.Epic -> EggLayoutModel(
            startGradient = WalkieTheme.colors.orange100,
            endGradient = WalkieTheme.colors.orange100,
            eggDrawable = R.drawable.img_egg_epic_crop,
            effectDrawable = R.drawable.img_effect_epic
        )

        EggKind.Legend -> EggLayoutModel(
            startGradient = WalkieTheme.colors.purple200,
            endGradient = WalkieTheme.colors.purple100,
            eggDrawable = R.drawable.img_egg_legend_crop,
            effectDrawable = R.drawable.img_effect_legend
        )
    }
}