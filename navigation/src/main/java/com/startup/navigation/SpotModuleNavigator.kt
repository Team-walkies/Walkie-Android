package com.startup.navigation

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher

interface SpotModuleNavigator {
    fun navigateSpotView(launcher: ActivityResultLauncher<Intent>, activity: Activity)
    fun navigateSpotModifyView(
        launcher: ActivityResultLauncher<Intent>,
        activity: Activity,
        intentBuilder: Intent.() -> Intent = { this }
    )
}