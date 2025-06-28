package com.startup.design_system.widget.modal

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.startup.design_system.R
import com.startup.design_system.ui.WalkieTheme
import com.startup.design_system.ui.noRippleClickable

@Composable
fun EventModal(
    title: Int = R.string.event_gift_title,
    subTitle: Int = R.string.event_until_end,
    positiveText: Int = R.string.dialog_positive,
    negativeText: Int? = null,
    dayCount: Int? = null,
    imageRes: Int? = null,
    backOrOutSideDismissBlock: Boolean = false,
    onClickPositive: () -> Unit,
    onClickNegative: (() -> Unit)? = null,
    onDismiss: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val imageHeight = remember { screenHeight * 0.25f }
    val imageWidth = remember { imageHeight * 1.18f }
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = !backOrOutSideDismissBlock,
            dismissOnClickOutside = !backOrOutSideDismissBlock,
        )
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = WalkieTheme.colors.white),
            shape = RoundedCornerShape(24.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 0.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                    .background(WalkieTheme.colors.white),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                imageRes?.let { imageResId ->
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = "Event Icon",
                        modifier = Modifier
                            .height(imageHeight)
                            .width(imageWidth)
                            .aspectRatio(1.18f)
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    stringResource(title),
                    style = WalkieTheme.typography.head5.copy(
                        color = WalkieTheme.colors.gray700,
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(subTitle),
                        style = WalkieTheme.typography.body2.copy(
                            color = WalkieTheme.colors.blue400,
                            textAlign = TextAlign.Center,
                        )
                    )
                    dayCount?.let { count ->
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .background(
                                    WalkieTheme.colors.blue50,
                                    RoundedCornerShape(4.dp)
                                )
                                .wrapContentWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.event_day_count, count),
                                style = WalkieTheme.typography.body2.copy(
                                    color = WalkieTheme.colors.blue400,
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                    }
                }
                Spacer(Modifier.height(20.dp))
                if (negativeText != null && onClickNegative != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    WalkieTheme.colors.gray100,
                                    RoundedCornerShape(12.dp)
                                )
                                .weight(1F)
                                .padding(vertical = 13.dp)
                                .noRippleClickable {
                                    onClickNegative.invoke()
                                }
                        ) {
                            Text(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .align(Alignment.Center),
                                text = stringResource(negativeText),
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
                                .weight(1F)
                                .padding(vertical = 13.dp)
                                .noRippleClickable {
                                    onClickPositive.invoke()
                                }
                        ) {
                            Text(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .align(Alignment.Center),
                                text = stringResource(positiveText),
                                style = WalkieTheme.typography.body1,
                                textAlign = TextAlign.Center,
                                color = WalkieTheme.colors.white,
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .background(
                                WalkieTheme.colors.blue300,
                                RoundedCornerShape(12.dp)
                            )
                            .fillMaxWidth()
                            .padding(vertical = 13.dp)
                            .noRippleClickable {
                                onClickPositive.invoke()
                            }
                    ) {
                        Text(
                            modifier = Modifier
                                .wrapContentSize()
                                .align(Alignment.Center),
                            text = stringResource(positiveText),
                            style = WalkieTheme.typography.body1,
                            textAlign = TextAlign.Center,
                            color = WalkieTheme.colors.white,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewEventModal() {
    WalkieTheme {
        EventModal(
            title = R.string.event_gift_title,
            positiveText = R.string.event_show,
            negativeText = R.string.dialog_dismiss,
            dayCount = 21,
            imageRes = R.drawable.img_gift,
            onDismiss = {},
            onClickPositive = {}
        )
    }
}

@Preview
@Composable
fun PreviewEventModalTwoButton() {
    WalkieTheme {
        EventModal(
            title = R.string.event_gift_title,
            subTitle = R.string.event_until_end,
            positiveText = R.string.event_show,
            negativeText = R.string.dialog_dismiss,
            dayCount = 21,
            imageRes = R.drawable.img_gift,
            onDismiss = {},
            onClickPositive = {},
            onClickNegative = {}
        )
    }
}