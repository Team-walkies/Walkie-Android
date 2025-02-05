package com.startup.common.util

import android.util.Log
import com.startup.common.BuildConfig

object Printer {
    private val isRelease = !BuildConfig.DEBUG
    fun e(tag: String, message: String) {
        if (isRelease) return
        Log.e(tag, message)
    }

    fun d(tag: String, message: String) {
        if (isRelease) return
        Log.d(tag, message)
    }

    fun i(tag: String, message: String) {
        if (isRelease) return
        Log.i(tag, message)
    }
}