package com.startup.home.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.startup.common.base.NavigationEvent
import com.startup.design_system.widget.actionbar.PageActionBar
import com.startup.design_system.widget.actionbar.PageActionBarType
import com.startup.design_system.widget.toggle.ToggleWithText
import com.startup.home.R
import com.startup.home.mypage.model.MyInfoUIEvent
import com.startup.home.mypage.model.MyInfoViewState
import com.startup.home.mypage.model.MyInfoViewStateImpl
import com.startup.home.mypage.model.PushSettingUIEvent
import com.startup.ui.WalkieTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PushSettingScreen(
    viewState: MyInfoViewState,
    uiEventSender: (PushSettingUIEvent) -> Unit,
    onNavigationEvent: (NavigationEvent) -> Unit
) {
    /*val isTodayStepNotiEnabled by viewState.isNotificationEnabledTodayStep.collectAsState()*/
    val isArriveSpotNotiEnabled by viewState.isNotificationEnabledSpotArrive.collectAsState()
    val isEggHatchedNotiEnabled by viewState.isNotificationEnabledEggHatched.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = WalkieTheme.colors.white)
    ) {
        PageActionBar(
            PageActionBarType.TitleActionBarType(
                { onNavigationEvent.invoke(NavigationEvent.Back) },
                title = stringResource(R.string.push_setting_title)
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            /*ToggleWithText(
                title = stringResource(R.string.push_setting_today_step_title),
                subTitle = stringResource(R.string.push_setting_today_step_sub_title),
                checked = isTodayStepNotiEnabled,
                onCheckedChanged = {
                    uiEventSender.invoke(PushSettingUIEvent.OnChangedTodayStepNoti(it))
                }
            )*/
            ToggleWithText(
                title = stringResource(R.string.push_setting_spot_arrive_title),
                subTitle = stringResource(R.string.push_setting_spot_arrive_sub_title),
                checked = isArriveSpotNotiEnabled,
                onCheckedChanged = {
                    uiEventSender.invoke(PushSettingUIEvent.OnChangedArriveSpotNoti(it))
                }
            )
            ToggleWithText(
                title = stringResource(R.string.push_setting_egg_hatch_title),
                subTitle = stringResource(R.string.push_setting_egg_hatch_sub_title),
                checked = isEggHatchedNotiEnabled,
                onCheckedChanged = {
                    uiEventSender.invoke(PushSettingUIEvent.OnChangedEggHatchedNoti(it))
                }
            )
        }
    }
}


@PreviewScreenSizes
@Composable
private fun PreviewPushSettingScreen() {
    WalkieTheme {
        PushSettingScreen(
            MyInfoViewStateImpl(
                isNotificationEnabledEggHatched = MutableStateFlow(false),
                isProfileAccess = MutableStateFlow(false),
                isNotificationEnabledTodayStep = MutableStateFlow(false),
                isNotificationEnabledSpotArrive = MutableStateFlow(false),
                userInfo = MutableStateFlow(null)
            ), {}, {})
    }
}