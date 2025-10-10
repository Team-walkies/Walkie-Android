package com.startup.design_system.widget.bottom_sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.startup.design_system.ui.WalkieTheme
import com.startup.design_system.widget.button.PrimaryButton
import com.startup.resource.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepRecordIntroductionBottomSheet(
    onDismiss: () -> Unit,
    onStartUsing: () -> Unit,
    onLater: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { true }
    )
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val lottieHeight = screenHeight * 0.43f

    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("walkie_healthcare1_1.json.json")
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = WalkieTheme.colors.white,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(WalkieTheme.colors.blue50)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = stringResource(R.string.step_record_new_label),
                    style = WalkieTheme.typography.caption1,
                    color = WalkieTheme.colors.blue400
                )
            }
            Spacer(Modifier.height(14.dp))
            Text(
                text = stringResource(R.string.step_record_intro_title),
                style = WalkieTheme.typography.head3,
                color = WalkieTheme.colors.gray700,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.step_record_intro_description),
                style = WalkieTheme.typography.body2,
                color = WalkieTheme.colors.gray500,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(20.dp))
            // (높이 = 스크린 높이 * 0.43, 너비 = 높이 * 0.86, 반복재생)
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier
                    .height(lottieHeight)
                    .aspectRatio(0.86f),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(8.dp))
            PrimaryButton(
                text = stringResource(R.string.step_record_start_button),
                onClick = onStartUsing,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.step_record_later_button),
                style = WalkieTheme.typography.body2,
                color = WalkieTheme.colors.gray500,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable { onLater() }
            )
        }
    }
}

@Preview
@Composable
fun PreviewStepRecordIntroductionBottomSheet() {
    WalkieTheme {
        StepRecordIntroductionBottomSheet(
            onDismiss = {},
            onStartUsing = {},
            onLater = {}
        )
    }
}