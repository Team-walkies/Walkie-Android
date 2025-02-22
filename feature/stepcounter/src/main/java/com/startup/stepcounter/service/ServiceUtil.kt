package com.startup.stepcounter.service

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.startup.common.util.OsVersions
import com.startup.common.util.UsePermissionHelper.isGrantedPermissions

object ServiceUtil {

    fun startForegroundService(context: Context) {
        val intent = Intent(context, WalkieStepForegroundService::class.java)

        try {
            val hasPermission = !OsVersions.isGreaterThanOrEqualsQ() ||
                    isGrantedPermissions(context, Manifest.permission.ACTIVITY_RECOGNITION)


            if (hasPermission) {
                ContextCompat.startForegroundService(context, intent)
            } else {
                com.startup.stepcounter.notification.showPermissionNotification(context)
            }
        } catch (e: Exception) {
            // 서비스 실행 실패시 재실행 alarmManger 등록
            val restartTime = System.currentTimeMillis() + 3000

            val restartIntent = Intent(context, com.startup.stepcounter.broadcastReciver.RestartServiceReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                restartIntent,
                PendingIntent.FLAG_IMMUTABLE
            )

            (context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager)?.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                restartTime,
                pendingIntent
            )
        }
    }
}