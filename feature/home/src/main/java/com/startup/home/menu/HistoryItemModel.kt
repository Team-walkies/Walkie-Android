package com.startup.home.menu

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.startup.home.R

data class HistoryItemModel(
    @DrawableRes val thumbnailDrawable : Int,
    @StringRes val titleString: Int,
    @StringRes val unitString : Int
)

fun getDefaultHistoryMenu(): List<HistoryItemModel>{
    return listOf(
        HistoryItemModel(
            thumbnailDrawable = R.drawable.img_gain_eggs,
            titleString = R.string.home_gain_eggs,
            unitString = R.string.quantity_string
        ),
        HistoryItemModel(
            thumbnailDrawable = R.drawable.img_hatching_characters,
            titleString = R.string.home_hatching_characters,
            unitString = R.string.group_characters_string
        ),
        HistoryItemModel(
            thumbnailDrawable = R.drawable.img_spot_history,
            titleString = R.string.home_spot_history,
            unitString = R.string.quantity_string
        )
    )
}