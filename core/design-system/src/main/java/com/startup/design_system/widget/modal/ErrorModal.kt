package com.startup.design_system.widget.modal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun ErrorModal(
    title: String = "",
    subTitle: String = "",
    positiveText: String = "",
    onClickPositive: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        )
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = WalkieTheme.colors.white),
            shape = RoundedCornerShape(12.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 24.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                    .background(WalkieTheme.colors.white),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (title.isNotEmpty()) {
                    Text(
                        title,
                        style = WalkieTheme.typography.head4.copy(color = WalkieTheme.colors.gray700)
                    )
                }
                if (subTitle.isNotEmpty()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = subTitle,
                        style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray500),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .background(
                            WalkieTheme.colors.red100,
                            RoundedCornerShape(12.dp)
                        )
                        .noRippleClickable {
                            onClickPositive.invoke()
                        }
                        .fillMaxWidth()
                        .padding(vertical = 13.dp)
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
        }
    }
}

@Preview
@Composable
fun PreviewErrorModal() {
    WalkieTheme {
        ErrorModal(
            title = stringResource(R.string.dialog_user_account_withdrawn_exception_title),
            subTitle = stringResource(R.string.dialog_user_account_withdrawn_exception_subtitle),
            positiveText = "확인",
            onDismiss = {},
            onClickPositive = {}
        )
    }
}