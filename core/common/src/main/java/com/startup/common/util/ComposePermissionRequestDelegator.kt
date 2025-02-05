package com.startup.common.util

import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.startup.common.extension.moveToAppDetailSetting

abstract class ComposePermissionRequestDelegator(
    private val activity: ComponentActivity,
    doOnGranted: () -> Unit,
    doOnShouldShowRequestPermissionRationale: (List<String>) -> Unit = {},
    doOnNeverAskAgain: (List<String>) -> Unit = {
        activity.moveToAppDetailSetting() // 앱 세부 설정으로 이동하는 함수
    },
) : BaseComposePermissionRequestDelegator(
    doOnGranted = doOnGranted,
    doOnNeverAskAgain = doOnNeverAskAgain,
    doOnShouldShowRequestPermissionRationale = doOnShouldShowRequestPermissionRationale,
) {
    override fun shouldShowRequestPermissionRationale(permission: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    }
}

@Composable
fun rememberPermissionRequestDelegator(
    permission: UsePermissionHelper.Permission,
    doOnGranted: () -> Unit,
    doOnShouldShowRequestPermissionRationale: (List<String>) -> Unit = {},
    doOnNeverAskAgain: (List<String>) -> Unit = {},
): ComposePermissionRequestDelegator {
    val context = LocalContext.current as ComponentActivity
    fun shouldShowRequestPermissionRationale(permission: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(context, permission)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultMap ->
        val deniedPermissions = resultMap
            .filter { it.value.not() }
            .map { it.key }

        if (deniedPermissions.isEmpty()) {
            doOnGranted()
            return@rememberLauncherForActivityResult
        }

        deniedPermissions
            .filter { shouldShowRequestPermissionRationale(it) }
            .takeIf { it.isNotEmpty() }
            ?.let { doOnShouldShowRequestPermissionRationale(it) }

        deniedPermissions
            .filterNot { shouldShowRequestPermissionRationale(it) }
            .takeIf { it.isNotEmpty() }
            ?.let { doOnNeverAskAgain(it) }
    }
    return remember {
        object : ComposePermissionRequestDelegator(
            activity = context,
            doOnGranted = doOnGranted,
            doOnShouldShowRequestPermissionRationale = doOnShouldShowRequestPermissionRationale,
            doOnNeverAskAgain = doOnNeverAskAgain
        ) {
            override val requestPermissionType: UsePermissionHelper.Permission
                get() = permission
            override val permissionLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>> = permissionLauncher
        }
    }
}