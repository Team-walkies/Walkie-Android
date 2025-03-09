package com.startup.walkie.navigationimpl

import android.content.Context
import android.content.Intent
import com.startup.navigation.AuthFlowNavigator
import com.startup.walkie.login.LoginActivity
import javax.inject.Inject

class AuthFlowNavigatorImpl @Inject constructor() : AuthFlowNavigator {
    override fun moveToLoginActivity(context: Context) {
        context.startActivity(Intent(context, LoginActivity::class.java))
    }
}