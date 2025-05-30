package com.startup.home.permission

import android.content.Context
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.startup.common.util.UsePermissionHelper
import com.startup.design_system.ui.WalkieTheme
import com.startup.design_system.widget.modal.PrimaryTwoButtonModal
import com.startup.home.R

/**
 * 권한 관련 UI 컴포넌트들을 처리하는 별도 컴포넌트
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HandlePermissionComponents(
    context: Context,
    permissionManager: PermissionManager
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val permissionUiState = permissionManager.permissionUiState
    val permissionStates = permissionManager.permissionStates

    // 권한 설정 다이얼로그
    if (permissionUiState.showPermissionSettingsDialog) {
        val activityPermissionGranted = UsePermissionHelper.isGrantedPermissions(
            context,
            *UsePermissionHelper.getTypeOfPermission(UsePermissionHelper.Permission.ACTIVITY_RECOGNITION)
        )

        val locationPermissionGranted = UsePermissionHelper.isGrantedPermissions(
            context,
            *UsePermissionHelper.getTypeOfPermission(UsePermissionHelper.Permission.FOREGROUND_LOCATION)
        )

        val bothPermissionsDenied = !activityPermissionGranted && !locationPermissionGranted
        val onlyActivityDenied = !activityPermissionGranted && locationPermissionGranted
        val onlyLocationDenied = activityPermissionGranted && !locationPermissionGranted

        val titleResId = when {
            bothPermissionsDenied -> R.string.permission_essential_dialog_title
            onlyActivityDenied -> R.string.permission_activity_recognition_title
            onlyLocationDenied -> R.string.permission_location_dialog_title
            else -> R.string.permission_essential_dialog_title
        }

        val messageResId = when {
            bothPermissionsDenied -> R.string.permission_essential_dialog_message
            onlyActivityDenied -> R.string.permission_activity_recognition_message
            onlyLocationDenied -> R.string.permission_location_dialog_message
            else -> R.string.permission_essential_dialog_message
        }

        val textAlign = when {
            bothPermissionsDenied || onlyActivityDenied -> TextAlign.Center
            onlyLocationDenied -> TextAlign.Start
            else -> TextAlign.Center
        }

        PrimaryTwoButtonModal(
            title = stringResource(titleResId),
            subTitle = stringResource(messageResId),
            negativeText = stringResource(R.string.permission_dialog_negative),
            positiveText = stringResource(R.string.permission_dialog_positive),
            onClickNegative = {
                permissionManager.closePermissionSettingsDialog()
            },
            onClickPositive = {
                permissionManager.closePermissionSettingsDialog(goToSettings = true)
            },
            textAlign = textAlign
        )
    }

    // 필수 권한 바텀 시트
    if (permissionUiState.showEssentialPermissionSheet) {
        PermissionBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                permissionManager.handleEssentialPermissionDismiss()
            }
        ) {
            EssentialPermissionBottomSheet(
                permissions = permissionStates,
                onAllPermissionsGranted = {
                    permissionManager.onEssentialPermissionsGranted()
                },
                onShowRationale = {
                    permissionManager.handlePermissionRationale()
                },
                onNeverAskAgain = {
                    permissionManager.handleNeverAskAgainPermissions()
                }
            )
        }
    }

    // 알림 권한 바텀 시트
    if (permissionUiState.showNotificationPermissionSheet) {
        PermissionBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                permissionManager.handleNotificationPermissionEvents(
                    PermissionManager.NotificationAction.DISMISS
                )
            }
        ) {
            NotificationPermissionBottomSheet(
                onDismiss = {
                    permissionManager.handleNotificationPermissionEvents(
                        PermissionManager.NotificationAction.DISMISS
                    )
                },
                onAllowPermission = {
                    permissionManager.handleNotificationPermissionEvents(
                        PermissionManager.NotificationAction.ALLOW
                    )
                },
                onShowRationale = {
                    permissionManager.handleNotificationPermissionEvents(
                        PermissionManager.NotificationAction.SHOW_RATIONALE
                    )
                },
                onNeverAskAgain = {
                    permissionManager.handleNotificationPermissionEvents(
                        PermissionManager.NotificationAction.NEVER_ASK_AGAIN
                    )
                }
            )
        }
    }
}

/**
 * 권한 관련 바텀시트 컴포넌트들
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