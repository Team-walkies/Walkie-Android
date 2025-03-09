package com.startup.navigation

import android.content.Context

interface HomeFeatureNavigator {
    fun navigateSpotView(context: Context)
    fun navigateLoginView(context: Context)
}