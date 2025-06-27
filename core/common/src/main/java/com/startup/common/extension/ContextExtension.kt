package com.startup.common.extension

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log

fun Context.moveToAppDetailSetting() {
    try {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.parse("package:$packageName")
            startActivity(this)
        }
    } catch (e: Exception) {
        Log.e("ContextExtension", "Context.moveToAppDetailSetting() Error")
    }
}

fun Context.moveToNotificationSetting() {
    try {
        Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            startActivity(this)
        }
    } catch (e: Exception) {
        Log.e("ContextExtension", "Context.moveToAppDetailSetting() Error")
    }
}

fun Context.openBrowser(url: String) {
    try {
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(webIntent)
    } catch (e: Exception) {
        Log.e("LMH", "url 이 유효하지 않음")
    }
}

fun Context.getAppVersion(): String {
    return try {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        packageInfo.versionName ?: "1.0.0"
    } catch (e: PackageManager.NameNotFoundException) {
        "1.0.0"
    } catch (e: Exception) {
        "1.0.0"
    }
}