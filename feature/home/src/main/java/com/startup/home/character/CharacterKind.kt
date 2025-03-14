package com.startup.home.character

import androidx.annotation.StringRes
import com.startup.home.R

enum class CharacterKind(
    @StringRes val displayStrResId: Int,
    @StringRes val characterStrResId: Int = R.string.character
) {
    Normal(R.string.clazz_normal),
    Rare(R.string.clazz_rare),
    Epic(R.string.clazz_epic),
    Legend(R.string.clazz_legend);
}
