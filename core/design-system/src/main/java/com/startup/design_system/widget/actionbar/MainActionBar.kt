package com.startup.design_system.widget.actionbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.design_system.R
import com.startup.ui.WalkieTheme
import com.startup.ui.noRippleClickable

@Composable
fun MainActionBar(isExistAlarm: Boolean, onClickAlarm: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = WalkieTheme.colors.white)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Spacer(modifier = Modifier.weight(1F))
        Box(
            modifier = Modifier
                .size(24.dp)
        ) {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .noRippleClickable { onClickAlarm.invoke() },
                painter = painterResource(R.drawable.ic_alert),
                tint = WalkieTheme.colors.gray400,
                contentDescription = null
            )
            if (isExistAlarm) {
                Box(
                    Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 3.dp, top = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(
                                color = WalkieTheme.colors.red100,
                                shape = RoundedCornerShape(10.dp)
                            )
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun PreviewMainActionBar() {
    WalkieTheme {
        Column(verticalArrangement = Arrangement.spacedBy(30.dp)) {
            MainActionBar(isExistAlarm = true) {}
            MainActionBar(isExistAlarm = false) {}
        }
    }
}