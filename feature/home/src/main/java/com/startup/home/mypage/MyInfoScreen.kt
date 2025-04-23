package com.startup.home.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.startup.common.base.NavigationEvent
import com.startup.common.util.Printer
import com.startup.design_system.widget.actionbar.PageActionBar
import com.startup.design_system.widget.actionbar.PageActionBarType
import com.startup.design_system.widget.toggle.ToggleWithText
import com.startup.home.R
import com.startup.home.mypage.model.MyInfoUIEvent
import com.startup.home.mypage.model.MyInfoViewState
import com.startup.home.mypage.model.MyInfoViewStateImpl
import com.startup.design_system.ui.WalkieTheme
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun MyInfoScreen(
    viewState: MyInfoViewState,
    uiEventSender: (MyInfoUIEvent) -> Unit,
    onNavigationEvent: (NavigationEvent) -> Unit
) {
    val isProfileAccess by viewState.isProfileAccess.collectAsState()
    Printer.e("LMH", "isProfileAccess $isProfileAccess")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = WalkieTheme.colors.white)
    ) {
        PageActionBar(
            PageActionBarType.TitleActionBarType(
                { onNavigationEvent.invoke(NavigationEvent.Back) },
                title = stringResource(R.string.my_info_title)
            )
        )
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.my_info_profile_access_title),
                style = WalkieTheme.typography.head4.copy(color = WalkieTheme.colors.gray700)
            )
            Spacer(modifier = Modifier.height(12.dp))
            ToggleWithText(
                title = stringResource(R.string.my_info_profile_access_toggle_title),
                subTitle = stringResource(R.string.my_info_profile_access_toggle_sub_title),
                checked = isProfileAccess,
                onCheckedChanged = {
                    Printer.e("LMH", "CHECK CHANGED $it")
                    uiEventSender.invoke(
                        MyInfoUIEvent.OnChangedProfileAccessToggle(
                            it
                        )
                    )
                }
            )
        }
    }
}


@PreviewScreenSizes
@Composable
private fun PreviewMyInfoScreen() {
    WalkieTheme {
        MyInfoScreen(
            viewState = MyInfoViewStateImpl(
                isNotificationEnabledEggHatched = MutableStateFlow(false),
                isProfileAccess = MutableStateFlow(false),
                isNotificationEnabledTodayStep = MutableStateFlow(false),
                isNotificationEnabledSpotArrive = MutableStateFlow(false),
                userInfo = MutableStateFlow(null)
            ),
            {}, {}
        )
    }
}