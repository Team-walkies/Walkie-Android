package com.startup.home.mypage.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.common.base.NavigationEvent
import com.startup.design_system.ui.WalkieTheme
import com.startup.design_system.widget.actionbar.PageActionBar
import com.startup.design_system.widget.actionbar.PageActionBarType
import com.startup.design_system.widget.textfield.WalkieTextArea
import com.startup.design_system.widget.modal.PrimaryModal
import com.startup.home.R

@Composable
fun RequestUserOpinionScreen(
    onNavigationEvent: (NavigationEvent) -> Unit
) {
    var opinionText by remember { mutableStateOf("") }
    var showModal by remember { mutableStateOf(false) }
    val maxLength = 500

    if (showModal) {
        PrimaryModal(
            title = stringResource(id = R.string.request_opinion_modal_title),
            subTitle = stringResource(id = R.string.request_opinion_modal_content),
            positiveText = stringResource(id = R.string.dialog_positive),
            onDismiss = { showModal = false },
            onClickPositive = {
                showModal = false
                onNavigationEvent(NavigationEvent.Back)
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = WalkieTheme.colors.white)
    ) {
        PageActionBar(
            pageActionBarType = PageActionBarType.TitleAndOptionalRightActionBarType(
                onBackClicked = { onNavigationEvent(NavigationEvent.Back) },
                title = stringResource(id = R.string.request_opinion_page_title),
                rightActionTitle = stringResource(id = R.string.request_opinion_submit),
                enabled = opinionText.isNotBlank(),
                enabledTextColor = WalkieTheme.colors.blue400,
                disableTextColor = WalkieTheme.colors.gray400,
                rightActionClicked = {
                    // TODO: 의견 제출 로직 구현
                    showModal = true
                }
            )
        )

        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(id = R.string.request_opinion_description),
                style = WalkieTheme.typography.head4.copy(color = WalkieTheme.colors.gray700)
            )

            Spacer(modifier = Modifier.height(32.dp))

            WalkieTextArea(
                value = opinionText,
                onValueChange = { opinionText = it },
                placeholder = stringResource(id = R.string.request_opinion_placeholder),
                maxLength = maxLength
            )
        }
    }
}

@Preview
@Composable
private fun PreviewRequestUserOpinionScreen() {
    WalkieTheme {
        RequestUserOpinionScreen(
            onNavigationEvent = {}
        )
    }
}
