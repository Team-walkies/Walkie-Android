package com.startup.common.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator

/**
 * 진동 효과 유틸리티
 */
@SuppressLint("MissingPermission")
fun triggerVibration(context: Context) {
    if (OsVersions.isGreaterThanOrEqualsS()) {
        val vibratorManager =
            context.getSystemService(android.os.VibratorManager::class.java)
        val vibrator = vibratorManager.defaultVibrator
        vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
    } else {
        val vibrator =
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (OsVersions.isGreaterThanOrEqualsO()) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    50,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            vibrator.vibrate(50)
        }
    }
}
