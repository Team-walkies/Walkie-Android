package com.startup.model.notification

import androidx.annotation.DrawableRes
import com.startup.model.R

enum class NotificationImageType(@DrawableRes val resId: Int) {
    EGG(R.drawable.ic_notification_egg),
    WALK(R.drawable.ic_notification_walk),
    SPOT(R.drawable.ic_notification_visit),
}

data class NotificationListItemModel(
    val id: Int,
    val imageType: NotificationImageType,
    val title: String,
    val content: String,
    val timeLapse: String
)