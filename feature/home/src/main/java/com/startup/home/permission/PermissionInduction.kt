package com.startup.home.permission

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.common.extension.moveToNotificationSetting
import com.startup.common.util.BatteryOptimizationHelper
import com.startup.common.util.UsePermissionHelper
import com.startup.common.util.rememberPermissionRequestDelegator
import com.startup.design_system.ui.WalkieTheme
import com.startup.design_system.widget.button.PrimaryButton
import com.startup.home.R

data class PermissionState(
    val type: UsePermissionHelper.Permission,
    val isGranted: Boolean,
    val essential: Boolean = true,
    @StringRes val title: Int,
    @StringRes val description: Int,
    @DrawableRes val iconRes: Int
)

/**
 * 바텀시트 공통 컨테이너 컴포넌트
 */
@Composable
private fun PermissionBottomSheetContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                WalkieTheme.colors.white,
                RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
            .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        content()
    }
}

/**
 * 권한 요청을 처리하는 바텀시트
 *
 * @param permissions 요청할 권한 목록
 * @param onAllPermissionsGranted 모든 권한이 허용되었을 때 호출할 콜백
 * @param onShowRationale 권한 설명이 필요할 때 호출할 콜백
 * @param onNeverAskAgain 영구 거절된 경우 호출할 콜백
 */
@Composable
fun EssentialPermissionBottomSheet(
    permissions: List<PermissionState>,
    onAllPermissionsGranted: () -> Unit,
    onShowRationale: (List<String>) -> Unit = {},
    onNeverAskAgain: (List<String>) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val permissionsState =
        remember { mutableStateListOf<PermissionState>().apply { addAll(permissions) } }

    val requiredPermissions = permissionsState.filter { !it.isGranted }

    val normalPermissions = requiredPermissions.filter {
        it.type != UsePermissionHelper.Permission.BATTERY_OPTIMIZATION
    }

    val hasBatteryOptimization = requiredPermissions.any {
        it.type == UsePermissionHelper.Permission.BATTERY_OPTIMIZATION
    }

    // 시스템 권한 타입 목록
    val systemPermissionTypes = normalPermissions.map { it.type }

    val permissionDelegator = if (systemPermissionTypes.isNotEmpty()) {
        rememberPermissionRequestDelegator(
            permissions = systemPermissionTypes,
            doOnGranted = {
                systemPermissionTypes.forEach { permissionType ->
                    val index = permissionsState.indexOfFirst { it.type == permissionType }
                    if (index != -1) {
                        permissionsState[index] = permissionsState[index].copy(isGranted = true)
                    }
                }

                if (hasBatteryOptimization && !BatteryOptimizationHelper.isBatteryOptimizationIgnored(
                        context
                    )
                ) {
                    openBatteryOptimizationSettings(context)

                    val batteryIndex = permissionsState.indexOfFirst {
                        it.type == UsePermissionHelper.Permission.BATTERY_OPTIMIZATION
                    }
                    if (batteryIndex != -1) {
                        permissionsState[batteryIndex] =
                            permissionsState[batteryIndex].copy(isGranted = true)
                    }
                }

                if (permissionsState.all { it.isGranted }) {
                    onAllPermissionsGranted()
                }
            },
            doOnShouldShowRequestPermissionRationale = onShowRationale,
            doOnNeverAskAgain = onNeverAskAgain
        )
    } else null

    PermissionBottomSheetContainer(modifier = modifier) {
        Text(
            text = stringResource(R.string.permission_bottomsheet_title),
            style = WalkieTheme.typography.head4.copy(color = WalkieTheme.colors.gray700),
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = stringResource(R.string.permission_bottomsheet_subtitle),
            style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray500),
        )

        Spacer(modifier = Modifier.height(20.dp))

        permissionsState.forEach { permission ->
            if (!permission.isGranted) PermissionItem(permissionState = permission)
        }

        Spacer(modifier = Modifier.height(20.dp))

        PrimaryButton(
            text = stringResource(R.string.permission_bottomsheet_check),
            onClick = {
                if (permissionDelegator != null) {
                    permissionDelegator.requestPermissionLauncher()
                } else if (hasBatteryOptimization && !BatteryOptimizationHelper.isBatteryOptimizationIgnored(
                        context
                    )
                ) {
                    // 시스템 권한은 없고 배터리 최적화만 필요한 경우
                    openBatteryOptimizationSettings(context)

                    val batteryIndex = permissionsState.indexOfFirst {
                        it.type == UsePermissionHelper.Permission.BATTERY_OPTIMIZATION
                    }
                    if (batteryIndex != -1) {
                        permissionsState[batteryIndex] =
                            permissionsState[batteryIndex].copy(isGranted = true)
                    }

                    if (permissionsState.all { it.isGranted }) {
                        onAllPermissionsGranted()
                    }
                } else if (permissionsState.all { it.isGranted }) {
                    onAllPermissionsGranted()
                }
            }
        )
    }
}

/**
 * 알림 권한 요청을 위한 바텀시트
 *
 * @param onDismiss 취소 시 호출할 콜백
 * @param onAllowPermission 권한 허용 시 호출할 콜백
 * @param onShowRationale 권한 설명이 필요할 때 호출할 콜백
 * @param onNeverAskAgain 다시 묻지 않음으로 설정된 경우 호출할 콜백
 * @param modifier 컴포넌트에 적용할 Modifier
 */
@Composable
fun NotificationPermissionBottomSheet(
    onDismiss: () -> Unit,
    onAllowPermission: () -> Unit,
    onShowRationale: (List<String>) -> Unit = {},
    onNeverAskAgain: (List<String>) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val notificationPermissionDelegator = rememberPermissionRequestDelegator(
        permissions = listOf(UsePermissionHelper.Permission.POST_NOTIFICATIONS),
        doOnGranted = onAllowPermission,
        doOnShouldShowRequestPermissionRationale = onShowRationale,
        doOnNeverAskAgain = {
            context.moveToNotificationSetting()
            onNeverAskAgain(it)
        }
    )

    PermissionBottomSheetContainer(modifier = modifier) {
        Text(
            text = stringResource(R.string.permission_notification_title),
            textAlign = TextAlign.Center,
            style = WalkieTheme.typography.head4.copy(color = WalkieTheme.colors.gray700),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.permission_notification_subtitle),
            style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray500),
        )
        Spacer(Modifier.height(20.dp))
        Image(
            painter = painterResource(R.drawable.img_notification_permission),
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp),
                onClick = onDismiss,
                enabled = true,
                colors = ButtonColors(
                    contentColor = WalkieTheme.colors.gray500,
                    containerColor = WalkieTheme.colors.gray100,
                    disabledContentColor = WalkieTheme.colors.gray500,
                    disabledContainerColor = WalkieTheme.colors.gray100
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    stringResource(R.string.permission_bottomsheet_later),
                    style = WalkieTheme.typography.body1
                )
            }

            PrimaryButton(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.permission_bottomsheet_notification),
                onClick = {
                    notificationPermissionDelegator?.requestPermissionLauncher()
                }
            )
        }
    }
}

/**
 * 권한 항목 컴포넌트
 */
@Composable
fun PermissionItem(
    permissionState: PermissionState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color = WalkieTheme.colors.gray50),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 4.dp)
        ) {
            Image(
                modifier = Modifier.size(18.dp),
                painter = painterResource(permissionState.iconRes),
                contentDescription = "Permission status",
            )

            Spacer(Modifier.width(4.dp))

            Text(
                text = stringResource(permissionState.title),
                style = WalkieTheme.typography.head6.copy(color = WalkieTheme.colors.gray700),
            )

            Spacer(Modifier.width(4.dp))
            if (permissionState.essential) {
                Text(
                    text = stringResource(R.string.permission_essential),
                    style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.blue400),
                )
            } else {
                Text(
                    text = stringResource(R.string.permission_optional),
                    style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray500),
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
            text = stringResource(permissionState.description),
            style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray500),
        )
    }
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
        EssentialPermissionBottomSheet(
            permissions = listOf(
                PermissionState(
                    type = UsePermissionHelper.Permission.ACTIVITY_RECOGNITION,
                    isGranted = false,
                    essential = true,
                    title = R.string.permission_activity_recognition,
                    description = R.string.permission_activity_recognition_description,
                    iconRes = R.drawable.ic_activity
                ),
                PermissionState(
                    type = UsePermissionHelper.Permission.FOREGROUND_LOCATION,
                    isGranted = false,
                    essential = true,
                    title = R.string.permission_location,
                    description = R.string.permission_location_description,
                    iconRes = R.drawable.ic_loaction_permission
                ),
                PermissionState(
                    type = UsePermissionHelper.Permission.BATTERY_OPTIMIZATION,
                    isGranted = false,
                    essential = false,
                    title = R.string.permission_battery_optimization,
                    description = R.string.permission_battery_optimization_description,
                    iconRes = R.drawable.ic_battery
                )
            ),
            onAllPermissionsGranted = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PermissionBottomSheetNotification() {
    WalkieTheme {
        NotificationPermissionBottomSheet(
            onDismiss = {},
            onAllowPermission = {}
        )
    }
}