package com.startup.home.permission

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.startup.common.base.UiEvent
import com.startup.common.util.BatteryOptimizationHelper
import com.startup.common.util.OsVersions
import com.startup.common.util.UsePermissionHelper
import com.startup.home.R


/**
 * 권한 관리만을 담당하는 독립적인 클래스
 */
class PermissionManager(
    private val context: Context,
    private val callbacks: PermissionManagerCallbacks
) {

    interface PermissionManagerCallbacks {
        fun startStepCounterService()
        fun emitUiEvent(event: UiEvent)
        fun launchPermissionSettings()
        fun launchBatteryOptimizationSettings()
    }

    // 권한 상태 데이터 클래스
    data class PermissionUiState(
        val showEssentialPermissionSheet: Boolean = false,
        val showNotificationPermissionSheet: Boolean = false,
        val showPermissionSettingsDialog: Boolean = false
    )

    // 현재 권한 상태
    var permissionUiState by mutableStateOf(PermissionUiState())
    var permissionStates by mutableStateOf<List<PermissionState>>(emptyList())

    /**
     * 주요 권한이 모두 허용되었는지 확인
     */
    private fun areAllEssentialPermissionsGranted(): Boolean {
        return permissionStates.all { it.isGranted }
    }

    /**
     * 배터리 최적화 제외 권한 확인
     */
    private fun areSystemPermissionsGranted(): Boolean {
        return permissionStates
            .filter { it.type != UsePermissionHelper.Permission.BATTERY_OPTIMIZATION }
            .all { it.isGranted }
    }

    private fun isBatteryOptimizationGranted(): Boolean {
        return BatteryOptimizationHelper.isBatteryOptimizationIgnored(context)
    }

    /**
     * 권한이 허용되었는지 확인
     */
    private fun isGranted(permission: UsePermissionHelper.Permission): Boolean {
        val permissions = UsePermissionHelper.getTypeOfPermission(permission)
        return UsePermissionHelper.isGrantedPermissions(context, *permissions)
    }

    /**
     * 모든 권한 확인 및 초기화
     */
    fun checkPermissions() {
        initPermissionStates()
        when {
            areAllEssentialPermissionsGranted() -> {
                proceedToNotification()
                callbacks.startStepCounterService()
            }

            !areSystemPermissionsGranted() -> {
                updateUiState { it.copy(showEssentialPermissionSheet = true) }
            }

            !isBatteryOptimizationGranted() -> {
                updateUiState { it.copy(showEssentialPermissionSheet = true) }
            }

            else -> {
                proceedToNotification()
            }
        }
    }

    fun proceedToBatteryOptimization() {
        if (!isBatteryOptimizationGranted()) {
            callbacks.launchBatteryOptimizationSettings()
        } else {
            proceedToNotification()
        }
    }

    fun proceedToNotification() {
        if (OsVersions.isGreaterThanOrEqualsTIRAMISU() &&
            !isGranted(UsePermissionHelper.Permission.POST_NOTIFICATIONS)
        ) {
            updateUiState { it.copy(showNotificationPermissionSheet = true) }
        } else {
            callbacks.startStepCounterService()
        }
    }

    /**
     * 권한 상태 초기화
     */
    private fun initPermissionStates() {
        permissionStates = buildList {
            add(
                PermissionState(
                    type = UsePermissionHelper.Permission.FOREGROUND_LOCATION,
                    isGranted = isGranted(UsePermissionHelper.Permission.FOREGROUND_LOCATION),
                    title = R.string.permission_location,
                    description = R.string.permission_location_description,
                    iconRes = R.drawable.ic_loaction_permission
                )
            )
            add(
                PermissionState(
                    type = UsePermissionHelper.Permission.ACTIVITY_RECOGNITION,
                    isGranted = isGranted(UsePermissionHelper.Permission.ACTIVITY_RECOGNITION),
                    title = R.string.permission_activity_recognition,
                    description = R.string.permission_activity_recognition_description,
                    iconRes = R.drawable.ic_activity
                )
            )
            add(
                PermissionState(
                    type = UsePermissionHelper.Permission.BATTERY_OPTIMIZATION,
                    essential = false,
                    isGranted = BatteryOptimizationHelper.isBatteryOptimizationIgnored(context),
                    title = R.string.permission_battery_optimization,
                    description = R.string.permission_battery_optimization_description,
                    iconRes = R.drawable.ic_battery
                )
            )
        }
    }

    /**
     * 권한 UI 상태 업데이트
     */
    private fun updateUiState(update: (PermissionUiState) -> PermissionUiState) {
        permissionUiState = update(permissionUiState)
    }

    /**
     * 바텀시트 닫은 후 권한 체크용
     */
    fun handleEssentialPermissionDismiss() {
        updateUiState { it.copy(showEssentialPermissionSheet = false) }

        when {
            isGranted(UsePermissionHelper.Permission.ACTIVITY_RECOGNITION) -> {
                callbacks.startStepCounterService()
            }

            !isGranted(UsePermissionHelper.Permission.ACTIVITY_RECOGNITION) -> {
                callbacks.emitUiEvent(PermissionUiEvent.ShowActivityRecognitionAlert(true))
            }

            !isGranted(UsePermissionHelper.Permission.BACKGROUND_LOCATION) -> {
                callbacks.emitUiEvent(PermissionUiEvent.ShowBackgroundLocationAlert(true))
            }
        }

        proceedToNotification()
    }

    fun onEssentialPermissionsGranted() {
        callbacks.startStepCounterService()
        updateUiState { it.copy(showEssentialPermissionSheet = false) }
        proceedToBatteryOptimization()
    }

    fun handleNeverAskAgainPermissions() {
        updateUiState {
            it.copy(
                showEssentialPermissionSheet = false,
                showPermissionSettingsDialog = true
            )
        }
    }

    fun handlePermissionRationale() {
        updateUiState {
            it.copy(
                showEssentialPermissionSheet = false,
            )
        }
        proceedToBatteryOptimization()
    }

    /**
     * 신체권한은 중요해서 onResume에서 허용안내 및 만약 허용되면 서비스 재개 로직 추가
     * 서비스가 실행된 상태에서 startStepCounterService 를 호출해도 중복 시작등은 발생하지 않음
     */
    fun checkOnResume() {
        val isActivityRecognitionGranted = isGranted(UsePermissionHelper.Permission.ACTIVITY_RECOGNITION)
        val isBackgroundLocationGranted = isGranted(UsePermissionHelper.Permission.BACKGROUND_LOCATION)

        if (isActivityRecognitionGranted) {
            callbacks.startStepCounterService()
        }

        callbacks.emitUiEvent(
            PermissionUiEvent.UpdateAllPermissionAlerts(
                showActivityAlert = !isActivityRecognitionGranted,
                showLocationAlert = !isBackgroundLocationGranted
            )
        )
    }

    /**
     * 알림 권한 관련 이벤트 처리
     */
    fun handleNotificationPermissionEvents(action: NotificationAction) {
        updateUiState { it.copy(showNotificationPermissionSheet = false) }
    }

    /**
     * 권한 설정 다이얼로그 닫기
     */
    fun closePermissionSettingsDialog(goToSettings: Boolean = false) {
        updateUiState { it.copy(showPermissionSettingsDialog = false) }
        if (goToSettings) {
            callbacks.launchPermissionSettings()
        } else {
            proceedToBatteryOptimization()
        }
    }

    /**
     * 알림 권한 처리를 위한 액션 정의
     */
    enum class NotificationAction {
        DISMISS, ALLOW, SHOW_RATIONALE, NEVER_ASK_AGAIN
    }
}