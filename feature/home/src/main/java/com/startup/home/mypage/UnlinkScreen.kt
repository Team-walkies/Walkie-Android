package com.startup.home.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.startup.common.base.NavigationEvent
import com.startup.design_system.widget.actionbar.PageActionBar
import com.startup.design_system.widget.actionbar.PageActionBarType
import com.startup.design_system.widget.button.DangerButton
import com.startup.design_system.widget.checkbox.TextCheckBox
import com.startup.home.R
import com.startup.home.mypage.model.UnlinkUiEvent
import com.startup.ui.WalkieTheme

@Composable
fun UnlinkScreen(
    userNickName: String,
    onNavigationEvent: (NavigationEvent) -> Unit,
    uiEventSender: (UnlinkUiEvent) -> Unit
) {
    var isCheckedNotice by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = WalkieTheme.colors.white)
    ) {
        PageActionBar(
            PageActionBarType.TitleActionBarType(
                onBackClicked = { onNavigationEvent.invoke(NavigationEvent.Back) },
                title = stringResource(R.string.unlink_header_title)
            )
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = stringResource(R.string.unlink_title, userNickName),
                style = WalkieTheme.typography.head3.copy(color = WalkieTheme.colors.gray700),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = WalkieTheme.colors.gray50,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.unlink_description_title_1),
                    style = WalkieTheme.typography.head6.copy(color = WalkieTheme.colors.gray700),
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.unlink_description_sub_title_1),
                    style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray500)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = WalkieTheme.colors.gray50,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.unlink_description_title_2),
                    style = WalkieTheme.typography.head6.copy(color = WalkieTheme.colors.gray700)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.unlink_description_sub_title_2),
                    style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray500)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            TextCheckBox(
                text = stringResource(R.string.unlink_approve_notice),
                checked = isCheckedNotice,
                onCheckedChanged = {
                    isCheckedNotice = it
                })
            Spacer(modifier = Modifier.weight(1F))
            DangerButton(
                modifier = Modifier.padding(bottom = 30.dp),
                enabled = isCheckedNotice,
                text = stringResource(R.string.unlink)
            ) {
                uiEventSender.invoke(
                    UnlinkUiEvent.UnlinkWalkie
                )
            }

        }
    }
}

@PreviewScreenSizes
@Composable
private fun PreviewUnlinkScreen() {
    WalkieTheme {
        UnlinkScreen("승빈짱짱", {}, {})
    }
}