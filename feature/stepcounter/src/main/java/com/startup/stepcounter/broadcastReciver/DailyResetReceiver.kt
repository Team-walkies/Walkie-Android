package com.startup.stepcounter.broadcastReciver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference

@AndroidEntryPoint
class DailyResetReceiver : BroadcastReceiver() {

    interface OnDateChangedListener {
        fun onDateChanged()
    }

    private var onDateChangedListener: WeakReference<OnDateChangedListener>? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        when (intent.action) {
            Intent.ACTION_DATE_CHANGED -> {  // 동적 등록때만 정상동작
                notifyDateChanged()
            }
        }
    }

    private fun notifyDateChanged() {
        onDateChangedListener?.get()?.onDateChanged()
    }

    fun setOnDateChangedListener(listener: OnDateChangedListener) {
        this.onDateChangedListener = WeakReference(listener)
    }

    fun removeOnDateChangedListener() {
        this.onDateChangedListener = null
    }
}