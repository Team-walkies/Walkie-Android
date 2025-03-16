package com.startup.home.permission

import android.content.Context
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.common.util.BatteryOptimizationHelper
import com.startup.common.util.UsePermissionHelper
import com.startup.ui.WalkieTheme

/**
 * 권한 상태를 나타내는 데이터 클래스
 */
data class PermissionState(
    val type: UsePermissionHelper.Permission,
    val isGranted: Boolean,
    val title: String,
    val description: String
)

/**
 * 권한 요청을 처리하는 바텀시트
 */
@Composable
fun PermissionBottomSheet(
    permissions: List<PermissionState>,
    onAllPermissionsGranted: () -> Unit,
) {
    val context = LocalContext.current

    // 권한 상태 추적
    val permissionsState =
        remember { mutableStateListOf<PermissionState>().apply { addAll(permissions) } }

    // 모든 권한이 허용되었는지 확인
    val allPermissionsGranted = permissionsState.all { it.isGranted }

    // 권한 분류 - 배터리 최적화를 제외한 일반 권한
    val normalPermissions = permissionsState.filter {
        it.type != UsePermissionHelper.Permission.BATTERY_OPTIMIZATION && !it.isGranted
    }

    // 배터리 최적화 권한 필요 여부
    val hasBatteryOptimization = permissionsState.any {
        it.type == UsePermissionHelper.Permission.BATTERY_OPTIMIZATION && !it.isGranted
    }

    // 권한 거부 카운트 추적 (영구 거절 감지용)
    val permissionDeniedCount =
        remember { mutableStateMapOf<UsePermissionHelper.Permission, Int>() }

    // 일반 권한 요청을 위한 런처
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultMap ->
        updatePermissionStates(resultMap, permissionsState, permissionDeniedCount, context)

        // 모든 권한이 허용되었는지 확인
        if (permissionsState.all { it.isGranted }) {
            onAllPermissionsGranted()
        }
    }


    // todo 무조건 바꿔야함
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                WalkieTheme.colors.white,
                RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "앱 권한 설정",
            style = WalkieTheme.typography.head1,
            color = WalkieTheme.colors.gray700,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Text(
            text = "앱을 제대로 사용하기 위해 아래 권한이 필요합니다.",
            color = WalkieTheme.colors.blue300,
            style = WalkieTheme.typography.body2,
        )

        permissionsState.forEach { permission ->
            PermissionItemWithoutButton(permissionState = permission)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (!allPermissionsGranted) {
            Button(
                onClick = {
                    requestPermissions(
                        normalPermissions = normalPermissions,
                        hasBatteryOptimization = hasBatteryOptimization,
                        permissionLauncher = permissionLauncher,
                        context = context,
                        permissionsState = permissionsState
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = "모든 권한 허용하기")
            }

            Button(
                onClick = { openAppSettings(context) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "Settings",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "설정에서 권한 허용")
                }
            }
        }
    }
}

/**
 * 권한 상태 업데이트 함수
 */
private fun updatePermissionStates(
    resultMap: Map<String, Boolean>,
    permissionsState: SnapshotStateList<PermissionState>,
    permissionDeniedCount: SnapshotStateMap<UsePermissionHelper.Permission, Int>,
    context: Context
) {
    resultMap.forEach { (permission, isGranted) ->
        // 해당 권한에 맞는 Permission 타입 찾기
        val permissionType = permissionsState.find { state ->
            UsePermissionHelper.getTypeOfPermission(state.type).contains(permission)
        }?.type ?: return@forEach

        if (isGranted) {
            // 허용된 권한 업데이트
            val index = permissionsState.indexOfFirst { it.type == permissionType }
            if (index != -1) {
                permissionsState[index] = permissionsState[index].copy(isGranted = true)
            }
        } else {
            // 거부된 권한 카운트 증가
            permissionDeniedCount[permissionType] = (permissionDeniedCount[permissionType] ?: 0) + 1

            // 영구 거절의 경우 거부된 경우 설정으로 이동
            if ((permissionDeniedCount[permissionType] ?: 0) >= 2) {
                openAppSettings(context)
            }
        }
    }
}

/**
 * 권한 요청 함수
 */
private fun requestPermissions(
    normalPermissions: List<PermissionState>,
    hasBatteryOptimization: Boolean,
    permissionLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
    context: Context,
    permissionsState: SnapshotStateList<PermissionState>
) {
    // 일반 권한 요청
    if (normalPermissions.isNotEmpty()) {
        val permissionsToRequest = normalPermissions
            .flatMap { UsePermissionHelper.getTypeOfPermission(it.type).toList() }
            .toTypedArray()

        if (permissionsToRequest.isNotEmpty()) {
            permissionLauncher.launch(permissionsToRequest)
        }
    }

    // 배터리 최적화 권한 요청
    if (hasBatteryOptimization && !BatteryOptimizationHelper.isBatteryOptimizationIgnored(context)) {
        openBatteryOptimizationSettings(context)

        // 배터리 최적화 상태 업데이트
        val index = permissionsState.indexOfFirst {
            it.type == UsePermissionHelper.Permission.BATTERY_OPTIMIZATION
        }
        if (index != -1) {
            permissionsState[index] = permissionsState[index].copy(isGranted = true)
        }
    }
}

/**
 * 권한 항목 컴포넌트
 */
@Composable
fun PermissionItemWithoutButton(permissionState: PermissionState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (permissionState.isGranted) Icons.Default.Check else Icons.Default.Warning,
            contentDescription = "Permission status",
            tint = if (permissionState.isGranted) WalkieTheme.colors.gray400 else WalkieTheme.colors.blue300
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp)
        ) {
            Text(
                text = permissionState.title,
                style = WalkieTheme.typography.body1,
                color = WalkieTheme.colors.gray700
            )

            Text(
                text = permissionState.description,
                style = WalkieTheme.typography.caption1,
                color = WalkieTheme.colors.gray500
            )
        }
    }
}

/**
 * 시스템 설정의 앱 상세 화면으로 이동하는 함수
 */
fun openAppSettings(context: Context) {
    val intent = UsePermissionHelper.getPermissionSettingsIntent(context)
    context.startActivity(intent)
}

/**
 * 배터리 최적화 설정 화면으로 이동하는 함수
 */
fun openBatteryOptimizationSettings(context: Context) {
    val intent = BatteryOptimizationHelper.getBatteryOptimizationSettingsIntent(context)
    context.startActivity(intent)
}

@Preview(name = "All Permissions Granted", showBackground = true)
@Composable
fun PermissionBottomSheetAllGrantedPreview() {
    WalkieTheme {
        PermissionBottomSheet(
            permissions = listOf(
                PermissionState(
                    type = UsePermissionHelper.Permission.ACTIVITY_RECOGNITION,
                    isGranted = true,
                    title = "신체 활동 권한",
                    description = "걸음수 측정을 위해서 필요합니다."
                ),
                PermissionState(
                    type = UsePermissionHelper.Permission.POST_NOTIFICATIONS,
                    isGranted = true,
                    title = "카메라",
                    description = "정확한 걸음수 측정과 알림 수신을 위해 필요합니다."
                ),
                PermissionState(
                    type = UsePermissionHelper.Permission.BATTERY_OPTIMIZATION,
                    isGranted = true,
                    title = "배터리 최적화 제외",
                    description = "백그라운드에서 앱이 원활하게 작동하기 위해 필요합니다."
                )
            ),
            onAllPermissionsGranted = {}
        )
    }
}