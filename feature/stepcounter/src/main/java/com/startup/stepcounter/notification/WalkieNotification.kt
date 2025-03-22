package com.startup.stepcounter.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.startup.common.util.OsVersions
import com.startup.common.util.Printer
import com.startup.common.util.formatWithLocale
import com.startup.stepcounter.R
import com.startup.stepcounter.notification.NotificationCode.ACTIVITY_PERMISSION_NOTIFICATION_CHANNEL_ID
import com.startup.stepcounter.notification.NotificationCode.ACTIVITY_PERMISSION_NOTIFICATION_CHANNEL_NAME
import com.startup.stepcounter.notification.NotificationCode.ACTIVITY_PERMISSION_NOTIFICATION_DESCRIPTION
import com.startup.stepcounter.notification.NotificationCode.ACTIVITY_PERMISSION_NOTIFICATION_ID
import com.startup.stepcounter.notification.NotificationCode.WALKIE_STEP_NOTIFICATION_CHANNEL_ID
import com.startup.stepcounter.notification.NotificationCode.WALKIE_STEP_NOTIFICATION_ID


//todo 토스처럼 알림 취소 한 후 걸음수 측정될때마다 살아나게 할지 아님 취소하자마자 살아나게 할지 기획측 문의
@SuppressLint("RemoteViewLayout")
fun buildWalkieNotification(context: Context, step: Int, target: Int = 10000): Notification {
    createNotificationChannel(context)

    val pendingIntent = createPendingIntent(context)

    // 남은 걸음 수 계산
    val remainingSteps = maxOf(0, target - step)

    // 진행률 계산 (최대 100%)
    val progressPercent = minOf(100, (step.toFloat() / target.toFloat() * 100).toInt())

    // 걸음 수 포맷팅 (천 단위 콤마)
    val formattedSteps = remainingSteps.formatWithLocale()
    val customLayout = RemoteViews(context.packageName, R.layout.walkie_notification)

    // 다크 모드 감지
    val isNightMode = (context.resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

    with(context) {
        val mainTextColor = if (isNightMode) getColor(R.color.white) else getColor(R.color.gray_700)
        val secondaryTextColor =
            if (isNightMode) getColor(R.color.gray_300) else getColor(R.color.gray_700)
        val progressTextColor =
            if (isNightMode) getColor(R.color.gray_300) else getColor(R.color.gray_500)

        customLayout.setTextColor(R.id.steps_info, mainTextColor)
        customLayout.setTextColor(R.id.target_step, secondaryTextColor)
        customLayout.setTextColor(R.id.progress_percent, progressTextColor)
    }

    customLayout.setTextViewText(R.id.steps_info, formattedSteps)
    customLayout.setTextViewText(R.id.progress_percent, "$progressPercent%")

    customLayout.setProgressBar(R.id.progress_bar, 100, progressPercent, false)


    return NotificationCompat.Builder(context, WALKIE_STEP_NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_walkie_artwork)
        .setCustomContentView(customLayout)
        .setContentIntent(pendingIntent)
        .setOngoing(true)
        .setStyle(NotificationCompat.DecoratedCustomViewStyle())
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

fun updateStepNotification(context: Context, steps: Int, target: Int) {
    try {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(
            WALKIE_STEP_NOTIFICATION_ID,
            buildWalkieNotification(context, steps, target)
        )
    } catch (e: Exception) {
        Printer.e("UpdateNotification", e.toString())
    }
}


/**
 * 스팟 도착시 생성할 알림
 */
fun buildArriveNotification(context: Context): Notification {
    return NotificationCompat.Builder(context, NotificationCode.ARRIVE_NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_walkie_artwork)
        .setContentTitle(context.getString(R.string.notification_arrive_title))
        .setContentText(context.getString(R.string.notification_arrive_message))
        .setAutoCancel(true)
        .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
        .build()
}

/**
 * 도착 알림 채널 생성
 */
private fun createArriveNotificationChannel(context: Context) {
    val channel = NotificationChannel(
        NotificationCode.ARRIVE_NOTIFICATION_CHANNEL_ID,
        NotificationCode.ARRIVE_NOTIFICATION_CHANNEL_NAME,
        NotificationManager.IMPORTANCE_HIGH
    ).apply {
        description = NotificationCode.ARRIVE_NOTIFICATION_DESCRIPTION
        enableVibration(true)
        vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
    }

    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}

/**
 * 도착 알림을 발송하는 함수
 */
fun sendArriveNotification(context: Context) {
    createArriveNotificationChannel(context)

    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(
        NotificationCode.ARRIVE_NOTIFICATION_ID,
        buildArriveNotification(context)
    )
}

/**
 * 부화 완료시 생성할 알림
 */
fun buildHatchingNotification(context: Context): Notification {

    return NotificationCompat.Builder(context, ACTIVITY_PERMISSION_NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_walkie_artwork)
        .setContentTitle(context.getString(R.string.notification_hatching_title))
        .setContentText(context.getString(R.string.notification_hatching_message))
        .setAutoCancel(true)
        .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
        .build()
}

/**
 * 부화 알림 채널 생성
 */
private fun createHatchingNotificationChannel(context: Context) {
    val channel = NotificationChannel(
        NotificationCode.HATCHING_NOTIFICATION_CHANNEL_ID,
        NotificationCode.HATCHING_NOTIFICATION_CHANNEL_NAME,
        NotificationManager.IMPORTANCE_HIGH
    ).apply {
        description = NotificationCode.HATCHING_NOTIFICATION_DESCRIPTION
        enableVibration(true)
        vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
    }

    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}

/**
 * 부화 완료 알림을 발송하는 함수
 */
fun sendHatchingNotification(context: Context) {
    createHatchingNotificationChannel(context)

    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(
        NotificationCode.HATCHING_NOTIFICATION_ID,
        buildHatchingNotification(context)
    )
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

private fun buildPermissionInductionNotification(context: Context): Notification {
    createPermissionNotificationChannel(context)
    return NotificationCompat.Builder(context, ACTIVITY_PERMISSION_NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_walkie_artwork)
        .setContentTitle(context.getString(R.string.notification_permission_title))
        .setContentText(context.getString(R.string.notification_permission_message))
        .setAutoCancel(true)
        .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
        .build()
}

fun showPermissionNotification(context: Context) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val notification = buildPermissionInductionNotification(context)
    notificationManager.notify(ACTIVITY_PERMISSION_NOTIFICATION_ID, notification)
}