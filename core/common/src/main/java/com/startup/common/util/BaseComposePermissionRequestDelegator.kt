package com.startup.common.util

import android.annotation.SuppressLint
import androidx.activity.compose.ManagedActivityResultLauncher

abstract class BaseComposePermissionRequestDelegator(
    protected val doOnGranted: () -> Unit,
    protected val doOnShouldShowRequestPermissionRationale: (List<String>) -> Unit,
    protected val doOnNeverAskAgain: (List<String>) -> Unit,
) {
    protected val deniedBeforeLaunchedPermission = mutableListOf<String>()

    abstract val requestPermissionType: UsePermissionHelper.Permission

    abstract val permissionLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>

    abstract fun shouldShowRequestPermissionRationale(permission: String): Boolean

    @SuppressLint("ComposableNaming")
    fun requestPermissionLauncher() {
        val permission = UsePermissionHelper.getTypeOfPermission(requestPermissionType)
        permission
            .also { deniedBeforeLaunchedPermission.clear() }
            .filter { shouldShowRequestPermissionRationale(it) }
            .apply {
                deniedBeforeLaunchedPermission.addAll(this)
            }
        permissionLauncher.launch(permission)
    }
}