package com.startup.home.mypage

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.startup.common.extension.getAppVersion
import com.startup.common.extension.noRippleClickable
import com.startup.common.extension.openBrowser
import com.startup.common.extension.orZero
import com.startup.common.extension.withColor
import com.startup.design_system.ui.WalkieTheme
import com.startup.design_system.widget.actionbar.MainActionBar
import com.startup.design_system.widget.modal.ErrorTwoButtonModal
import com.startup.home.MyPageScreenNavigationEvent
import com.startup.home.R
import com.startup.home.mypage.model.MyInfoViewState
import com.startup.webview.WalkieWebConstants

@Composable
fun MyPageScreen(
    myInfoViewState: MyInfoViewState,
    onNavigationEvent: (MyPageScreenNavigationEvent) -> Unit
) {
    val context = LocalContext.current
    var isLogoutDialogShow by remember { mutableStateOf(false) }
    val userInfo by myInfoViewState.userInfo.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = WalkieTheme.colors.white)
    ) {
        MainActionBar(
            isExistAlarm = false,
            onClickAlarm = { onNavigationEvent.invoke(MyPageScreenNavigationEvent.MoveToNotification) })
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(state = rememberScrollState())
                .padding(bottom = 50.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = userInfo?.memberNickName.orEmpty(),
                    style = WalkieTheme.typography.head2.copy(color = WalkieTheme.colors.gray700)
                )
                Text(
                    text = stringResource(R.string.user),
                    style = WalkieTheme.typography.head2.copy(color = WalkieTheme.colors.gray500)
                )
                Spacer(modifier = Modifier.width(8.dp))
                UserTierTag(
                    userInfo?.memberTier.orEmpty(),
                    textColor = WalkieTheme.colors.blue400,
                    backgroundColor = WalkieTheme.colors.blue50
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                stringResource(
                    R.string.current_explored_spot_count_desc,
                    userInfo?.exploredSpot.orZero()
                ).withColor(userInfo?.exploredSpot.orZero().toString(), WalkieTheme.colors.blue400),
                style = WalkieTheme.typography.head5.copy(color = WalkieTheme.colors.gray500)
            )
            Spacer(modifier = Modifier.height(20.dp))
            // 설정
            SettingColumnComponent(stringResource(R.string.setting),
                listOf(
                    MenuItem(
                        type = MenuType.MyInfo,
                        titleStrResId = R.string.setting_my_info,
                        rightMenuType = RightMenuType.Arrow(),
                        clickEvent = { onNavigationEvent.invoke(MyPageScreenNavigationEvent.MoveToMyInfo) }
                    ),
                    MenuItem(
                        type = MenuType.Push,
                        titleStrResId = R.string.setting_push,
                        rightMenuType = RightMenuType.Arrow(),
                        clickEvent = { onNavigationEvent.invoke(MyPageScreenNavigationEvent.MoveToPushSetting) }
                    )
                ))

            Spacer(modifier = Modifier.height(8.dp))
            // 서비스 약관
            SettingColumnComponent(stringResource(R.string.term_of_service),
                listOf(
                    MenuItem(
                        type = MenuType.Notice,
                        titleStrResId = R.string.setting_notice,
                        rightMenuType = RightMenuType.Arrow(),
                        clickEvent = { onNavigationEvent.invoke(MyPageScreenNavigationEvent.MoveToNotice) }
                    ),
                    MenuItem(
                        type = MenuType.Policy,
                        titleStrResId = R.string.setting_policy,
                        rightMenuType = RightMenuType.Arrow(),
                        clickEvent = { onNavigationEvent.invoke(MyPageScreenNavigationEvent.MoveToPersonalInfoPolicy) }
                    ),
                    MenuItem(
                        type = MenuType.ServiceTerm,
                        titleStrResId = R.string.setting_service_term,
                        rightMenuType = RightMenuType.Arrow(),
                        clickEvent = { onNavigationEvent.invoke(MyPageScreenNavigationEvent.MoveToServiceTerm) }
                    ),
                    MenuItem(
                        type = MenuType.Version,
                        titleStrResId = R.string.setting_app_version,
                        rightMenuType = RightMenuType.Text("v${context.getAppVersion()}"),
                    )
                ))
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .background(
                        color = WalkieTheme.colors.gray50,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
                    .noRippleClickable {
                        context.openBrowser(WalkieWebConstants.FAQ_URL)
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_setting_survey),
                        contentDescription = stringResource(id = R.string.request_opinion_title)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1F)) {
                        Text(
                            text = stringResource(id = R.string.request_opinion_title),
                            style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray700)
                        )
                        Text(
                            text = stringResource(id = R.string.request_opinion_sub_title),
                            style = WalkieTheme.typography.caption1.copy(color = WalkieTheme.colors.gray500)
                        )
                    }
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        tint = WalkieTheme.colors.gray300,
                        contentDescription = null
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.noRippleClickable {
                        isLogoutDialogShow = true
                    },
                    text = stringResource(R.string.logout),
                    style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray400)
                )
                VerticalDivider(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .height(16.dp),
                    thickness = 1.dp,
                    color = WalkieTheme.colors.gray300
                )
                Text(
                    modifier = Modifier.noRippleClickable {
                        onNavigationEvent.invoke(MyPageScreenNavigationEvent.MoveToUnlink(userInfo?.memberNickName.orEmpty()))
                    },
                    text = stringResource(R.string.unlink),
                    style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray400)
                )
            }
        }
    }
    if (isLogoutDialogShow) {
        ErrorTwoButtonModal(
            title = stringResource(R.string.dialog_logout_title),
            subTitle = stringResource(R.string.dialog_logout_subtitle),
            negativeText = stringResource(R.string.dialog_logout_negative),
            positiveText = stringResource(R.string.dialog_logout_positive),
            onClickNegative = {
                isLogoutDialogShow = false
            },
            onClickPositive = {
                onNavigationEvent.invoke(MyPageScreenNavigationEvent.MoveToLoginActivityWithLogout)
                isLogoutDialogShow = false
            })
    }
}


private enum class MenuType(@DrawableRes val drawableResId: Int) {
    MyInfo(R.drawable.ic_setting_my), Push(R.drawable.ic_setting_push),
    Notice(R.drawable.ic_setting_notice), Policy(R.drawable.ic_setting_policy),
    ServiceTerm(R.drawable.ic_setting_service_term), Version(R.drawable.ic_setting_version)
}

private data class MenuItem(
    val type: MenuType,
    @StringRes val titleStrResId: Int,
    val rightMenuType: RightMenuType = RightMenuType.Arrow(),
    val clickEvent: () -> Unit = {}
)

private sealed interface RightMenuType {
    data class Text(val content: String) : RightMenuType
    data class Arrow(@DrawableRes val iconResId: Int = R.drawable.ic_arrow_right) : RightMenuType
    data object Not : RightMenuType
}

@Composable
private fun SettingColumnComponent(
    title: String,
    menuList: List<MenuItem>
) {
    Column(
        modifier = Modifier
            .background(
                color = WalkieTheme.colors.gray50,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = WalkieTheme.typography.head6.copy(color = WalkieTheme.colors.gray500)
        )
        MenuItemView(
            menuList
        )
    }
}

@Composable
private fun MenuItemView(list: List<MenuItem>) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
    ) {
        list.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .noRippleClickable {
                        item.clickEvent.invoke()
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(item.type.drawableResId),
                    contentDescription = stringResource(id = item.titleStrResId)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    modifier = Modifier.weight(1F),
                    text = stringResource(id = item.titleStrResId),
                    style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray700)
                )
                when (item.rightMenuType) {
                    RightMenuType.Not -> {}
                    is RightMenuType.Text -> {
                        Text(
                            text = item.rightMenuType.content,
                            style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray400)
                        )
                    }

                    is RightMenuType.Arrow -> {
                        Icon(
                            painter = painterResource(id = item.rightMenuType.iconResId),
                            tint = WalkieTheme.colors.gray300,
                            contentDescription = null
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun UserTierTag(text: String, textColor: Color, backgroundColor: Color) {
    Box(
        modifier = Modifier
            .background(backgroundColor, shape = RoundedCornerShape(8.dp))
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Text(text, style = WalkieTheme.typography.body2.copy(color = textColor))
    }
}

@Preview
@Composable
private fun PreviewMyPageScreen() {
    WalkieTheme {/*
        MyPageScreen(
            userInfo = UserInfo(
                memberId = 1,
                isPublic = true,
                exploredSpot = 5,
                recordedSpot = 2,
                provider = "",
                userCharacterId = 1,
                providerId = "",
                eggId = 1,
                memberNickName = "승빈짱짱",
                memberTier = "초보워키",
                memberEmail = ""
            )
        ) {}*/
    }
}