package com.startup.spot.navigation

import android.content.Context
import android.content.Intent
import com.startup.navigation.SpotModuleNavigator
import com.startup.spot.SpotActivity
import javax.inject.Inject

class SpotModuleNavigatorImpl @Inject constructor(): SpotModuleNavigator {
    override fun navigateSpotView(context: Context) {
        context.startActivity(Intent(context, SpotActivity::class.java))
    }
}