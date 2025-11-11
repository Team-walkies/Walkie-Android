package com.startup.design_system.widget.badge

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.design_system.R
import com.startup.design_system.ui.WalkieTheme

@Composable
fun EggBadge(modifier: Modifier = Modifier, status: EggBadgeStatus) {
    Image(modifier = modifier, painter = painterResource(status.eggResId), contentDescription = null)
}

enum class EggBadgeStatus(@DrawableRes val eggResId: Int, @StringRes val eggStrResId: Int) {
    PENDING(
        eggResId = R.drawable.ic_egg_badge_not_ready,
        eggStrResId = R.string.healthcare_egg_get
    ),
    AVAILABLE(
        eggResId = R.drawable.ic_egg_badge_get,
        eggStrResId = R.string.healthcare_egg_get
    ),
    RECEIVED(
        eggResId = R.drawable.ic_egg_badge_already,
        eggStrResId = R.string.healthcare_egg_got
    ),
    MISSED(
        eggResId = R.drawable.ic_eggbadge_broken,
        eggStrResId = R.string.healthcare_egg_missed
    )
}

@Composable
@Preview
fun PreviewEggBadge() {
    WalkieTheme {
        Row {
            EggBadgeStatus.entries.forEach {
                EggBadge(modifier = Modifier.size(20.dp), status = it)
            }
        }
    }
}