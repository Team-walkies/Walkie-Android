package com.startup.walkie.navigationimpl

import android.content.Context
import android.content.Intent
import com.startup.home.HomeActivity
import com.startup.navigation.LoginFeatureNavigator
import javax.inject.Inject

class LoginFeatureNavigatorImpl @Inject constructor(): LoginFeatureNavigator {
    override fun moveToHomeActivity(context: Context) {
        context.startActivity(Intent(context, HomeActivity::class.java))
    }
}