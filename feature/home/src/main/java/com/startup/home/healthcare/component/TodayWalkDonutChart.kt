package com.startup.home.healthcare.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.common.extension.formatWithLocale
import com.startup.common.extension.noRippleClickable
import com.startup.design_system.ui.WalkieTheme
import com.startup.design_system.widget.chart.DonutChart
import com.startup.home.R

@Composable
internal fun TodayWalkDonutChart(
    modifier: Modifier = Modifier,
    targetStep: Int,
    currentStep: Int,
    isToday: Boolean,
    onClickTargetStep: () -> Unit
) {
    val percentage = currentStep.toFloat() / targetStep
    val convertPercentage = if (percentage.isNaN()) 0F else percentage.coerceIn(0F, 1F)
    DonutChart(
        modifier = modifier.size(260.dp),
        strokeWidth = 26.dp,
        percentage = convertPercentage
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier
                        .background(color = WalkieTheme.colors.gray50, shape = CircleShape)
                        .padding(vertical = 2.dp, horizontal = 8.dp),
                    text = stringResource(R.string.healthcare_goal),
                    style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray400)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Row(
                    modifier = Modifier
                        .noRippleClickable {
                            if (isToday) {
                                onClickTargetStep.invoke()
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier,
                        text = targetStep.formatWithLocale(),
                        style = WalkieTheme.typography.body1.copy(color = WalkieTheme.colors.gray400)
                    )
                    if (isToday) {
                        Icon(
                            modifier = Modifier
                                .rotate(90F),
                            painter = painterResource(R.drawable.ic_chevron),
                            tint = WalkieTheme.colors.gray400,
                            contentDescription = null,
                        )
                    }
                }
            }
            Text(
                text = currentStep.formatWithLocale(),
                style = WalkieTheme.typography.head1.copy(color = WalkieTheme.colors.gray700)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewTodayWalkDonutChart() {
    WalkieTheme {
        Box(modifier = Modifier.background(WalkieTheme.colors.white)) {
            TodayWalkDonutChart(modifier = Modifier, 6000, 2515, isToday = true) {}
        }
    }
}