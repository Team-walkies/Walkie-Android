package com.startup.walkie.navigationimpl

import android.content.Context
import android.content.Intent
import com.startup.navigation.HomeFeatureNavigator
import com.startup.spot.SpotActivity
import com.startup.walkie.login.LoginActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeFeatureNavigatorImpl @Inject constructor() : HomeFeatureNavigator {
    override fun navigateSpotView(context: Context) {
        context.startActivity(Intent(context, SpotActivity::class.java))
    }

    override fun navigateLoginView(context: Context) {
        context.startActivity(Intent(context, LoginActivity::class.java))
    }
}