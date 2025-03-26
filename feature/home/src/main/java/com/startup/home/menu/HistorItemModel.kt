package com.startup.home.menu

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class HistoryItemModel(
    val myHistoryKind: MyHistoryKind,
    @DrawableRes val thumbnailDrawable : Int,
    @StringRes val titleString: Int,
    @StringRes val unitString : Int,
    val count : Int
) {
    enum class MyHistoryKind {
        GainEgg, GainCharacter, SpotArchive
    }
}
