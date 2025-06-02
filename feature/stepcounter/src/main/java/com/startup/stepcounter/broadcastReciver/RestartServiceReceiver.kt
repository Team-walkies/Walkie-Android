package com.startup.stepcounter.broadcastReciver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.startup.stepcounter.service.ServiceUtil.startStepTracking
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
서비스를 재실행 시켜주기 위한 브로드캐스트리시버
 */
@AndroidEntryPoint
internal class RestartServiceReceiver @Inject constructor() : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            startStepTracking(it)
        }
    }
}
