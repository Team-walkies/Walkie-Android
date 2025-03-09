package com.startup.navigation

import android.content.Context

interface AuthFlowNavigator {
    fun moveToLoginActivity(context: Context)
}