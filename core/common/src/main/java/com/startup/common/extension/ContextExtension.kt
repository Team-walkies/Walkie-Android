package com.startup.common.extension

import android.content.Context
import android.content.Intent
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

fun Context.openBrowser(url: String) {
    try {
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(webIntent)
    } catch (e: Exception) {
        Log.e("LMH", "url 이 유효하지 않음")
    }
}