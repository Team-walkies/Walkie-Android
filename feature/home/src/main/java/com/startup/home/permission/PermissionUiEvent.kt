package com.startup.home.permission

import com.startup.common.base.UiEvent

sealed interface PermissionUiEvent : UiEvent {
    data class ShowActivityRecognitionAlert(val show: Boolean) : PermissionUiEvent
}