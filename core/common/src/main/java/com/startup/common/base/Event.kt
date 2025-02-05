package com.startup.common.base


interface BaseEvent {
    object None : BaseEvent
}
interface Event : BaseEvent
interface NavigationEvent : BaseEvent {
    object Back : NavigationEvent
}
interface ScreenNavigationEvent : BaseEvent