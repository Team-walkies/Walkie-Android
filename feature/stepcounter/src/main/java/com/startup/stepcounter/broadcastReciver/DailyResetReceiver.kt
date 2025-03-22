package com.startup.stepcounter.broadcastReciver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.lang.ref.WeakReference

class DailyResetReceiver : BroadcastReceiver() {

    interface OnDateChangedListener {
        fun onDateChanged()
    }

    private var onDateChangedListener: WeakReference<OnDateChangedListener>? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return


        when (intent.action) {
            Intent.ACTION_DATE_CHANGED -> {
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