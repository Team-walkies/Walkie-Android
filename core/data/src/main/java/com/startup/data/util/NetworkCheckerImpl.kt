package com.startup.data.util

import android.content.Context
import android.net.ConnectivityManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkCheckerImpl @Inject constructor(@ApplicationContext private val context: Context) : NetworkChecker {
    override fun isNetworkAvailable() = context
        .getSystemService(ConnectivityManager::class.java)
        .activeNetwork != null
}