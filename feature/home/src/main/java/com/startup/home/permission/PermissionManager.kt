package com.startup.home.permission

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.startup.common.base.UiEvent
import com.startup.common.extension.moveToAppDetailSetting
import com.startup.common.util.BatteryOptimizationHelper
import com.startup.common.util.OsVersions
import com.startup.common.util.UsePermissionHelper
import com.startup.home.HomeActivity
import com.startup.home.R
import com.startup.home.main.HomeViewModel
import com.startup.ui.WalkieTheme


/**
 * 권한 관리 클래스 - 모든 권한 관련 로직을 캡슐화
 */
class PermissionManager(
    private val activity: HomeActivity,
    private val viewModel: HomeViewModel
) {
    // 권한 상태 데이터 클래스
    data class PermissionUiState(
        val showEssentialPermissionSheet: Boolean = false,
        val showActivityPermissionAlert: Boolean = false,
        val showNotificationPermissionSheet: Boolean = false,
        val showPermissionSettingsDialog: Boolean = false
    )

    // 현재 권한 상태
    var permissionUiState by mutableStateOf(PermissionUiState())

    var permissionStates by mutableStateOf<List<PermissionState>>(emptyList())

    /**
     * 필수 권한이 모두 허용되었는지 확인
     */
    private fun areAllEssentialPermissionsGranted(): Boolean {
        return permissionStates
            .all { it.isGranted }
    }

    /**
     * 권한이 허용되었는지 확인
     */
    private fun isGranted(permission: UsePermissionHelper.Permission): Boolean {
        val permissions = UsePermissionHelper.getTypeOfPermission(permission)
        return UsePermissionHelper.isGrantedPermissions(activity, *permissions)
    }

    /**
     * 모든 권한 확인 및 초기화
     */
    fun checkPermissions() {
        initPermissionStates()

        if (!areAllEssentialPermissionsGranted()) {
            updateUiState { it.copy(showEssentialPermissionSheet = true) }
        } else {
            updateUiState { it.copy(showEssentialPermissionSheet = false) }
            activity.startStepCounterService()
            checkNotificationPermission()
        }
    }

    /**
     * 권한 상태 초기화
     */
    private fun initPermissionStates() {
        val states = mutableListOf<PermissionState>()
        addActivityRecognitionPermission(states)
        addLocationPermission(states)
        addBatteryOptimizationPermission(states)
        permissionStates = states
    }

    private fun addActivityRecognitionPermission(states: MutableList<PermissionState>) {
        states.add(
            PermissionState(
                type = UsePermissionHelper.Permission.ACTIVITY_RECOGNITION,
                isGranted = isGranted(UsePermissionHelper.Permission.ACTIVITY_RECOGNITION),
                title = R.string.permission_activity_recognition,
                description = R.string.permission_activity_recognition_description,
                iconRes = R.drawable.ic_activity
            )
        )
    }

    private fun addLocationPermission(states: MutableList<PermissionState>) {
        states.add(
            PermissionState(
                type = UsePermissionHelper.Permission.FOREGROUND_LOCATION,
                isGranted = isGranted(UsePermissionHelper.Permission.FOREGROUND_LOCATION),
                title = R.string.permission_location,
                description = R.string.permission_location_description,
                iconRes = R.drawable.ic_loaction_permission
            )
        )
    }

    private fun addBatteryOptimizationPermission(states: MutableList<PermissionState>) {
        states.add(
            PermissionState(
                type = UsePermissionHelper.Permission.BATTERY_OPTIMIZATION,
                essential = false,
                isGranted = BatteryOptimizationHelper.isBatteryOptimizationIgnored(activity),
                title = R.string.permission_battery_optimization,
                description = R.string.permission_battery_optimization_description,
                iconRes = R.drawable.ic_battery
            )
        )
    }

    /**
     * 알림 권한 확인
     * 알림 권한은 별도의 플로우로 이루어져있기에 검사로직을 별도 추가
     */
    private fun checkNotificationPermission() {
        if (OsVersions.isGreaterThanOrEqualsTIRAMISU() &&
            !isGranted(UsePermissionHelper.Permission.POST_NOTIFICATIONS)
        ) {
            updateUiState { it.copy(showNotificationPermissionSheet = true) }
        }
    }

    /**
     * 권한 UI 상태 업데이트
     */
    private fun updateUiState(update: (PermissionUiState) -> PermissionUiState) {
        permissionUiState = update(permissionUiState)
    }

    /**
     * 권한 이벤트 발생 -> 홈화면에서 [신체활동 권한 허용 안내] 컴포넌트 노출 용
     */
    private fun emitEvent(event: UiEvent) {
        viewModel.emitUiEvent(event)
    }

    /**
     * 바텀시트 닫은 후 신체권한 체크용
     */
    fun handleEssentialPermissionDismiss() {
        if (areAllEssentialPermissionsGranted()) {
            activity.startStepCounterService()
        } else if (!isGranted(UsePermissionHelper.Permission.ACTIVITY_RECOGNITION)) {
            emitEvent(PermissionUiEvent.ShowActivityRecognitionAlert(true))
        }
    }

    fun onEssentialPermissionsGranted() {
        activity.startStepCounterService()
        updateUiState { it.copy(showEssentialPermissionSheet = false) }
        checkNotificationPermission()
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
        if (!isGranted(UsePermissionHelper.Permission.ACTIVITY_RECOGNITION)) {
            emitEvent(PermissionUiEvent.ShowActivityRecognitionAlert(true))
        }
        updateUiState {
            it.copy(
                showEssentialPermissionSheet = false,
            )
        }
        checkNotificationPermission()
    }


    /**
     * 신체권한은 중요해서 onResume에서 허용안내 및 만약 허용되면 서비스 재개 로직 추가
     * 서비스가 실행된 상태에서 startStepCounterService 를 호출해도 중복 시작등은 발생하지 않음
     */
    fun checkOnResume() {
        if (!isGranted(UsePermissionHelper.Permission.ACTIVITY_RECOGNITION)) {
            emitEvent(PermissionUiEvent.ShowActivityRecognitionAlert(true))
        } else {
            emitEvent(PermissionUiEvent.ShowActivityRecognitionAlert(false))
            activity.startStepCounterService()
        }
    }

    /**
     * 알림 권한 관련 이벤트 처리
     */
    fun handleNotificationPermissionEvents(action: NotificationAction) {
        updateUiState { it.copy(showNotificationPermissionSheet = false) }

        when (action) {
            NotificationAction.NEVER_ASK_AGAIN -> {
                // todo 오프모임때 논의 이후 추가로직 설정
                // updateUiState { it.copy(showPermissionSettingsDialog = true) }
            }

            else -> Unit
        }
    }

    /**
     * 권한 설정 다이얼로그 닫기
     */
    fun closePermissionSettingsDialog(goToSettings: Boolean = false) {
        updateUiState { it.copy(showPermissionSettingsDialog = false) }
        if (goToSettings) {
            activity.moveToAppDetailSetting()
        }
        checkNotificationPermission()
    }

    /**
     * 알림 권한 처리를 위한 액션 정의
     */
    enum class NotificationAction {
        DISMISS, ALLOW, SHOW_RATIONALE, NEVER_ASK_AGAIN
    }
}

/**
 * 권한 바텀시트 Composable
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionBottomSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        tonalElevation = 24.dp,
        dragHandle = null,
        containerColor = WalkieTheme.colors.white,
        contentColor = WalkieTheme.colors.white,
        scrimColor = WalkieTheme.colors.blackOpacity60,
        content = content
    )
}