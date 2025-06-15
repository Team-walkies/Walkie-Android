package com.startup.design_system.widget.bottom_sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.startup.design_system.R
import com.startup.design_system.ui.WalkieTheme
import com.startup.design_system.ui.noRippleClickable

@Composable
fun ForceUpdateBottomSheet(
    title: String,
    subTitle: String,
    negativeText: String,
    positiveText: String,
    exitApp: () -> Unit,
    goToPlayStore: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { }
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    WalkieTheme.colors.white,
                    RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = WalkieTheme.typography.head4.copy(
                        color = WalkieTheme.colors.gray700,
                        textAlign = TextAlign.Center
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subTitle,
                    style = WalkieTheme.typography.body2.copy(
                        color = WalkieTheme.colors.gray500,
                        textAlign = TextAlign.Center
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                WalkieTheme.colors.gray100,
                                RoundedCornerShape(12.dp)
                            )
                            .weight(1f)
                            .padding(vertical = 13.dp)
                            .noRippleClickable {
                                exitApp.invoke()
                            }
                    ) {
                        Text(
                            modifier = Modifier
                                .wrapContentSize()
                                .align(Alignment.Center),
                            text = negativeText,
                            style = WalkieTheme.typography.body1,
                            textAlign = TextAlign.Center,
                            color = WalkieTheme.colors.gray500,
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(
                                WalkieTheme.colors.blue300,
                                RoundedCornerShape(12.dp)
                            )
                            .weight(1f)
                            .padding(vertical = 13.dp)
                            .noRippleClickable {
                                goToPlayStore.invoke()
                            }
                    ) {
                        Text(
                            modifier = Modifier
                                .wrapContentSize()
                                .align(Alignment.Center),
                            text = positiveText,
                            style = WalkieTheme.typography.body1,
                            textAlign = TextAlign.Center,
                            color = WalkieTheme.colors.white,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Preview
@PreviewScreenSizes
@Composable
fun PreviewForceUpdateBottomSheet() {
    WalkieTheme {
        ForceUpdateBottomSheet(
            title = stringResource(R.string.bottomsheet_force_update_title),
            subTitle = stringResource(R.string.bottomsheet_force_update_sub_title),
            negativeText = stringResource(R.string.bottomsheet_force_update_negative_btn),
            positiveText = stringResource(R.string.dialog_force_update_positive_btn),
            exitApp = {},
            goToPlayStore = {}
        )
    }
}