package com.startup.stepcounter.broadcastReciver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.startup.domain.provider.DateChangeListener
import com.startup.domain.provider.DateChangeNotifier
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.ref.WeakReference
import javax.inject.Inject

@AndroidEntryPoint
class DailyResetReceiver @Inject constructor(
    @ApplicationContext private val context: Context
) : BroadcastReceiver(), DateChangeNotifier {

    private var listener: WeakReference<DateChangeListener>? = null
    private var isRegistered = false

    override fun setListener(listener: DateChangeListener) {
        this.listener = WeakReference(listener)
    }

    override fun removeListener() {
        this.listener = null
    }

    override fun startListening() {
        if (!isRegistered) {
            val filter = IntentFilter().apply {
                addAction(Intent.ACTION_DATE_CHANGED)
            }
            context.registerReceiver(this, filter)
            isRegistered = true
        }
    }

    override fun stopListening() {
        if (isRegistered) {
            try {
                context.unregisterReceiver(this)
                isRegistered = false
            } catch (e: Exception) {
                // 이미 unregister된 경우 무시
            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        when (intent.action) {
            Intent.ACTION_DATE_CHANGED -> {  // 동적 등록때만 정상동작
                notifyDateChanged()
            }
        }
    }

    private fun notifyDateChanged() {
        listener?.get()?.onDateChanged()
    }
}