package com.startup.stepcounter.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.text.HtmlCompat
import com.startup.common.util.OsVersions
import com.startup.common.util.Printer
import com.startup.stepcounter.notification.NotificationCode.ACTIVITY_PERMISSION_NOTIFICATION_CHANNEL_ID
import com.startup.stepcounter.notification.NotificationCode.ACTIVITY_PERMISSION_NOTIFICATION_CHANNEL_NAME
import com.startup.stepcounter.notification.NotificationCode.ACTIVITY_PERMISSION_NOTIFICATION_DESCRIPTION
import com.startup.stepcounter.notification.NotificationCode.ACTIVITY_PERMISSION_NOTIFICATION_ID
import com.startup.stepcounter.notification.NotificationCode.WALKIE_STEP_NOTIFICATION_CHANNEL_ID
import com.startup.stepcounter.notification.NotificationCode.WALKIE_STEP_NOTIFICATION_ID


//todo 토스처럼 알림 취소 한 후 걸음수 측정될때마다 살아나게 할지 아님 취소하자마자 살아나게 할지 기획측 문의
fun buildWalkieNotification(context: Context, step: Int): Notification {
    createNotificationChannel(context)

    val pendingIntent = createPendingIntent(context)

    val styledText = HtmlCompat.fromHtml(
        "<b>${step}</b> 걸음",
        HtmlCompat.FROM_HTML_MODE_LEGACY
    )

    return NotificationCompat.Builder(context, WALKIE_STEP_NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(com.startup.design_system.R.mipmap.ic_launcher_round)
        .setContentIntent(pendingIntent)
        .setOngoing(true)
        .setContentTitle("걸음 수")
        .setContentText(styledText)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
        .setCategory(NotificationCompat.CATEGORY_SERVICE)
        .build()
}

private fun createNotificationChannel(context: Context) {
    if (OsVersions.isGreaterThanOrEqualsO()) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            WALKIE_STEP_NOTIFICATION_CHANNEL_ID,
            "걸음 수 측정",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "걸음 수를 측정하는 서비스입니다"
            setShowBadge(true)
        }
        notificationManager.createNotificationChannel(channel)
    }
}

private fun createPendingIntent(context: Context): PendingIntent {
    val intent = Intent().apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    } // todo 펜딩인텐트 추가

    return PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )
}

fun updateStepNotification(context: Context, steps: Int) {
    try {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(
            WALKIE_STEP_NOTIFICATION_ID,
            buildWalkieNotification(context, steps)
        )
    } catch (e: Exception) {
        Printer.e("UpdateNotification", e.toString())
    }
}

private fun createDefaultNotification(context: Context): Notification {
    createNotificationChannel(context)

    return NotificationCompat.Builder(context, WALKIE_STEP_NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(com.startup.design_system.R.mipmap.ic_launcher)
        .setContentTitle("Walkie")
        .setContentText("워키!")
        .setOngoing(true)
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
        .setCategory(NotificationCompat.CATEGORY_SERVICE)
        .build()
}

fun buildNotificationWithPermission(context: Context): Notification {
    createPermissionNotificationChannel(context)

    return NotificationCompat.Builder(context, ACTIVITY_PERMISSION_NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(com.startup.design_system.R.mipmap.ic_launcher_round)
        .setContentTitle("걸음수 측정이 중지 되었어요.")
        .setContentText("걸음수 측정을 위해 신체권한 활동 권한을 허용해주세요")
        .setAutoCancel(true)
        .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
        .setColor(0xFF0DAEFF.toInt())
        .build()
}

private fun createPermissionNotificationChannel(context: Context) {
    if (OsVersions.isGreaterThanOrEqualsO()) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            ACTIVITY_PERMISSION_NOTIFICATION_CHANNEL_ID,
            ACTIVITY_PERMISSION_NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = ACTIVITY_PERMISSION_NOTIFICATION_DESCRIPTION
            enableVibration(true)
            setShowBadge(false)
        }
        notificationManager.createNotificationChannel(channel)
    }
}

fun showPermissionNotification(context: Context) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val notification = buildNotificationWithPermission(context)
    notificationManager.notify(ACTIVITY_PERMISSION_NOTIFICATION_ID, notification)
}