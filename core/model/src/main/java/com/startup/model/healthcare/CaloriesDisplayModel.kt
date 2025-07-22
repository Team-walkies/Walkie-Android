package com.startup.model.healthcare

data class CaloriesDisplayModel(
    val description: String,
    val title: String,
    val url: String,
) {
    companion object {
        fun orEmpty() : CaloriesDisplayModel = CaloriesDisplayModel(
            "슬슬 운동한 느낌 나죠?", "바나나 1개", ""
        )
    }
}