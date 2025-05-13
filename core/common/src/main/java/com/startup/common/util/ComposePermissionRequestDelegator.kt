package com.startup.common.util

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.startup.common.base.BaseComposePermissionRequestDelegator
import com.startup.common.extension.moveToAppDetailSetting
import java.lang.ref.WeakReference

/**
 * Context에서 ComponentActivity를 찾는 확장 함수
 */
fun Context.findComponentActivity(): ComponentActivity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is ComponentActivity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}


abstract class ComposePermissionRequestDelegator(
    private val activity: WeakReference<ComponentActivity>,
    doOnGranted: () -> Unit,
    doOnShouldShowRequestPermissionRationale: (List<String>) -> Unit = {},
    doOnNeverAskAgain: (List<String>) -> Unit = {
        activity.get()?.moveToAppDetailSetting() // 앱 세부 설정으로 이동하는 함수
    },
) : BaseComposePermissionRequestDelegator(
    doOnGranted = doOnGranted,
    doOnNeverAskAgain = doOnNeverAskAgain,
    doOnShouldShowRequestPermissionRationale = doOnShouldShowRequestPermissionRationale,
) {
    override fun shouldShowRequestPermissionRationale(permission: String): Boolean {
        return activity.get()
            ?.let { ActivityCompat.shouldShowRequestPermissionRationale(it, permission) } ?: false
    }
}

@Composable
fun rememberPermissionRequestDelegator(
    permissions: List<UsePermissionHelper.Permission>,
    doOnGranted: () -> Unit,
    doOnShouldShowRequestPermissionRationale: (List<String>) -> Unit = {},
    doOnNeverAskAgain: (List<String>) -> Unit = {},
): ComposePermissionRequestDelegator? {
    val context = LocalContext.current
    val componentActivity = context.findComponentActivity() ?: return null

    fun shouldShowRequestPermissionRationale(permission: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(componentActivity, permission)
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
    val activityRef = remember { WeakReference(componentActivity) }

    return remember {
        object : ComposePermissionRequestDelegator(
            activity = activityRef,
            doOnGranted = doOnGranted,
            doOnShouldShowRequestPermissionRationale = doOnShouldShowRequestPermissionRationale,
            doOnNeverAskAgain = doOnNeverAskAgain
        ) {
            override val requestPermissionTypes: List<UsePermissionHelper.Permission>
                get() = permissions
            override val permissionLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>> =
                permissionLauncher
        }
    }
}
