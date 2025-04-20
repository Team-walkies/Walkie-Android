package com.startup.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher

interface SpotModuleNavigator {
    fun navigateSpotView(context: Context)
    fun navigateSpotModifyView(
        launcher: ActivityResultLauncher<Intent>,
        activity: Activity,
        intentBuilder: Intent.() -> Intent = { this }
    )
}