package com.startup.spot.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.startup.navigation.SpotModuleNavigator
import com.startup.spot.SpotActivity
import com.startup.spot.modify.ModifyReviewActivity
import javax.inject.Inject

class SpotModuleNavigatorImpl @Inject constructor() : SpotModuleNavigator {
    override fun navigateSpotView(context: Context) {
        context.startActivity(Intent(context, SpotActivity::class.java))
    }

    override fun navigateSpotModifyView(
        launcher: ActivityResultLauncher<Intent>,
        activity: Activity,
        intentBuilder: Intent.() -> Intent
    ) {
        launcher.launch(Intent(activity, ModifyReviewActivity::class.java).intentBuilder())
    }
}