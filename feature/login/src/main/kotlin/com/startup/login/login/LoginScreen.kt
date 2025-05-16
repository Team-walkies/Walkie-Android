package com.startup.login.login

import android.app.Activity
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.design_system.widget.toast.ShowToast
import com.startup.login.R
import com.startup.design_system.ui.WalkieTheme
import com.startup.design_system.ui.noRippleClickable
import com.startup.login.login.model.LoginUiEvent
import com.startup.model.login.OnBoardingPagerItem
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(uiEventSender: (LoginUiEvent) -> Unit) {
    val onboardingList: List<OnBoardingPagerItem> = listOf(
        OnBoardingPagerItem(
            R.drawable.onboarding_01,
            title = stringResource(R.string.onboarding_1_title),
            description = stringResource(R.string.onboarding_1_sub_title)
        ),
        OnBoardingPagerItem(
            R.drawable.onboarding_02,
            title = stringResource(R.string.onboarding_2_title),
            description = stringResource(R.string.onboarding_2_sub_title)
        ),
        OnBoardingPagerItem(
            R.drawable.onboarding_03,
            title = stringResource(R.string.onboarding_3_title),
            description = stringResource(R.string.onboarding_3_sub_title)
        ),
        OnBoardingPagerItem(
            R.drawable.onboarding_04,
            title = stringResource(R.string.onboarding_4_title),
            description = stringResource(R.string.onboarding_4_sub_title)
        ),
    )

    val context = LocalContext.current
    var backPressedOnce by remember { mutableStateOf(false) }

    BackHandler {
        if (backPressedOnce) {
            (context as? Activity)?.finish()
        } else {
            backPressedOnce = true
        }
    }

    // 2초 후 플래그 리셋
    LaunchedEffect(backPressedOnce) {
        if (backPressedOnce) {
            delay(2000)
            backPressedOnce = false
        }
    }

    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    val maxOnboardingImageWidth = when {
        screenWidthDp > 800 -> {
            // 큰 태블릿
            780.dp
        }

        screenWidthDp > 600 -> {
            // 폴더블 or 작은 태블릿
            405.dp
        }

        else -> {
            // 일반 폰
            343.dp
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = WalkieTheme.colors.white),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val pagerState = rememberPagerState(
            initialPage = 0,
            pageCount = { onboardingList.size }
        )
        val currentPage = pagerState.currentPage
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(color = WalkieTheme.colors.white),
        ) { page ->
            val (drawable, title, subTitle) = onboardingList[page]
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(drawable), contentDescription = null,
                    modifier = Modifier.widthIn(max = maxOnboardingImageWidth)
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = title,
                    style = WalkieTheme.typography.head3.copy(color = WalkieTheme.colors.gray700),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = subTitle,
                    style = WalkieTheme.typography.body1.copy(color = WalkieTheme.colors.gray500),
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(modifier = Modifier.height(72.dp))
        Row(
            modifier = Modifier.wrapContentWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(onboardingList.size) {
                Box {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                color = WalkieTheme.colors.gray200,
                                shape = CircleShape
                            ),
                    )
                    if (it == currentPage) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    color = WalkieTheme.colors.blue300,
                                    shape = CircleShape
                                ),
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.weight(1F))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp, start = 16.dp, end = 16.dp)
                .background(
                    color = Color(0xFFFEE500),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(vertical = 15.dp)
                .noRippleClickable { uiEventSender.invoke(LoginUiEvent.OnClickLoginButton) },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_kakao),
                contentDescription = stringResource(R.string.desc_login_kakao)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.login_action_button),
                style = WalkieTheme.typography.body1.copy(color = WalkieTheme.colors.black)
            )
        }
    }

    if (backPressedOnce) {
        ShowToast(stringResource(R.string.toast_home_back_press), 2_000) {}
    }
}


@Composable
@Preview
fun PreviewLoginScreen() {
    WalkieTheme {
        LoginScreen({})
    }
}