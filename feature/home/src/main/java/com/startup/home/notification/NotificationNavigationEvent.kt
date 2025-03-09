package com.startup.home.notification

import com.startup.common.base.ScreenNavigationEvent

sealed interface NotificationNavigationEvent: ScreenNavigationEvent {
    data object Back : NotificationNavigationEvent
}