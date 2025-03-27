package com.startup.walkie.navigation

import android.content.Context
import android.content.Intent
import com.startup.navigation.LoginModuleNavigator
import com.startup.walkie.login.LoginActivity
import javax.inject.Inject

class LoginModuleNavigatorImpl @Inject constructor() : LoginModuleNavigator {
    override fun navigateLoginView(context: Context) {
        context.startActivity(Intent(context, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        })
    }
}