package com.startup.stepcounter.broadcastReciver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.startup.common.util.Printer
import com.startup.domain.provider.StepDataStore
import com.startup.stepcounter.service.StepDataStoreEntryPoint
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import javax.inject.Inject
import kotlin.math.acos

@AndroidEntryPoint
class DailyResetReceiver : BroadcastReceiver() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface StepDataStoreEntryPoint {
        fun stepDataStore(): StepDataStore
    }

    interface OnDateChangedListener {
        fun onDateChanged()
    }

    private var onDateChangedListener: WeakReference<OnDateChangedListener>? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return
        Printer.e("JUNWOO" , "action: ${intent.action}")

        when (intent.action) {
            Intent.ACTION_DATE_CHANGED -> {
                notifyDateChanged()

                val stepDataStore = EntryPointAccessors.fromApplication(
                    context.applicationContext,
                    StepDataStoreEntryPoint::class.java
                ).stepDataStore()

                CoroutineScope(Dispatchers.IO).launch {
                    stepDataStore.resetTodaySteps()
                }
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