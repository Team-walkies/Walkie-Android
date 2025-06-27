package com.startup.home.mypage.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.common.base.NavigationEvent
import com.startup.common.extension.noRippleClickable
import com.startup.common.util.Printer
import com.startup.design_system.ui.WalkieTheme
import com.startup.design_system.widget.actionbar.PageActionBar
import com.startup.design_system.widget.actionbar.PageActionBarType
import com.startup.design_system.widget.toggle.ToggleWithText
import com.startup.home.R
import com.startup.home.mypage.PushSettingUIEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PushSettingScreen(
    isNotificationEnabledEggHatchedFlow: StateFlow<Boolean>,
    isGrantNotificationPermissionFlow: StateFlow<Boolean>,
    uiEventSender: (PushSettingUIEvent) -> Unit,
    onNavigationEvent: (NavigationEvent) -> Unit
) {
    /*val isTodayStepNotiEnabled by viewState.isNotificationEnabledTodayStep.collectAsState()*/
    /*val isArriveSpotNotiEnabled by isNotificationEnabledSpotArriveFlow.collectAsState()*/
    val isEggHatchedNotiEnabled by isNotificationEnabledEggHatchedFlow.collectAsState()
    val isGrantNotificationPermission by isGrantNotificationPermissionFlow.collectAsState()
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
            Printer.e("LMH", "isGrantNotificationPermission $isGrantNotificationPermission")
            if (!isGrantNotificationPermission) {
                NotificationPermissionOffLabel {
                    uiEventSender.invoke(PushSettingUIEvent.OnClickMoveNotificationSetting)
                }
            }
            /*ToggleWithText(
                title = stringResource(R.string.push_setting_today_step_title),
                subTitle = stringResource(R.string.push_setting_today_step_sub_title),
                checked = isTodayStepNotiEnabled,
                onCheckedChanged = {
                    uiEventSender.invoke(PushSettingUIEvent.OnChangedTodayStepNoti(it))
                }
            )*/
            /*ToggleWithText(
                title = stringResource(R.string.push_setting_spot_arrive_title),
                subTitle = stringResource(R.string.push_setting_spot_arrive_sub_title),
                checked = isArriveSpotNotiEnabled,
                onCheckedChanged = {
                    uiEventSender.invoke(PushSettingUIEvent.OnChangedArriveSpotNoti(it))
                }
            )*/
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

@Preview(showBackground = true)
@Composable
private fun NotificationPermissionOffLabel(onClickMoveNotificationSetting: () -> Unit = {}) {
    WalkieTheme {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = WalkieTheme.colors.blue30,
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = WalkieTheme.colors.blue100
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(18.dp),
                painter = painterResource(R.drawable.ic_danger),
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = WalkieTheme.colors.gray400)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(R.string.push_setting_notification_permission_not_granted),
                style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray500)
            )
            Spacer(modifier = Modifier.weight(1F))
            Text(
                text = stringResource(R.string.push_setting_notification_permission_move_setting),
                style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.blue400),
                modifier = Modifier.noRippleClickable { onClickMoveNotificationSetting.invoke() }
            )
        }
    }
}

@Composable
private fun PreviewPushSettingScreen() {
    WalkieTheme {
        PushSettingScreen(
            MutableStateFlow(false),
            MutableStateFlow(false),
            {}, {},
        )
    }
}