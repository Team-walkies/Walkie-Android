package com.startup.stepcounter.broadcastReciver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.startup.stepcounter.service.ServiceUtil.startStepTracking
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * 기기 재부팅시 서비스 시작을 위한 BroadcastReceiver
 */
@AndroidEntryPoint
internal class BootCompletedReceiver @Inject constructor() : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            context?.let {
                startStepTracking(it)
            }
        }
    }
}