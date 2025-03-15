package com.startup.stepcounter.broadcastReciver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.startup.stepcounter.service.ServiceUtil.startForegroundService
import dagger.hilt.android.AndroidEntryPoint
import java.util.Objects
import javax.inject.Inject

/**
 * 앱 업데이트를 감지하여 서비스를 재시작하는 브로드캐스트 리시버
 */
@AndroidEntryPoint
class PackageChangedReceiver @Inject constructor() : BroadcastReceiver(){

    override fun onReceive(context: Context?, intent: Intent?) {
        if (Objects.equals(intent?.action, Intent.ACTION_MY_PACKAGE_REPLACED)) {
            context?.let {
                startForegroundService(it)
            }
        }
    }
}
