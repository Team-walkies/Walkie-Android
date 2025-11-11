package com.startup.home.healthcare.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.design_system.ui.WalkieTheme
import com.startup.design_system.widget.badge.EggBadgeStatus
import com.startup.design_system.widget.chart.DonutChart

@Composable
fun DailyStepGoalDonutChart(percentage: Float, @DrawableRes eggResId: Int) {
    val convertPercentage = if (percentage.isNaN()) 0F else percentage.coerceIn(0F, 1F)
    DonutChart(
        modifier = Modifier.size(36.dp),
        strokeWidth = 5.dp,
        percentage = convertPercentage,
    ) {
        if (eggResId != -1) {
            Image(
                modifier = Modifier.size(18.dp),
                painter = painterResource(eggResId),
                contentDescription = null
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDailyStepGoalDonutChart() {
    WalkieTheme {
        Box(modifier = Modifier.background(WalkieTheme.colors.white)) {
            DailyStepGoalDonutChart(1F, EggBadgeStatus.MISSED.eggResId)
        }
    }
}