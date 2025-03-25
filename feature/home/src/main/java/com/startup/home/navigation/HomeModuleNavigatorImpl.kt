package com.startup.home.navigation

import android.content.Context
import android.content.Intent
import com.startup.home.HomeActivity
import com.startup.navigation.HomeModuleNavigator
import javax.inject.Inject

class HomeModuleNavigatorImpl @Inject constructor(): HomeModuleNavigator{
    override fun moveToHomeActivity(context: Context) {
        context.startActivity(Intent(context, HomeActivity::class.java))
    }
}