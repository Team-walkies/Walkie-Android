package com.startup.home.permission

import com.startup.common.base.UiEvent

sealed interface PermissionUiEvent : UiEvent {
    data class ShowActivityRecognitionAlert(val show: Boolean) : PermissionUiEvent
    data class ShowBackgroundLocationAlert(val show: Boolean) : PermissionUiEvent

    data class UpdateAllPermissionAlerts(
        val showActivityAlert: Boolean,
        val showLocationAlert: Boolean
    ) : PermissionUiEvent
}