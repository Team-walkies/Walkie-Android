package com.startup.home.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.startup.design_system.R
import com.startup.ui.WalkieTheme
import com.startup.ui.noRippleClickable

sealed class BottomNavItem(
    val route: String,
    @DrawableRes val icon: Int,
    @StringRes val label: Int
) {
    data object Home : BottomNavItem("home", R.drawable.ic_home_off, R.string.navigation_home)
    data object Spot : BottomNavItem("spot", R.drawable.ic_map, R.string.navigation_spot)
    data object MyPage :
        BottomNavItem("mypage", R.drawable.ic_mypage_off, R.string.navigation_mypage)

    // 중앙 아이템인지 확인하는 함수
    fun isCenterItem(): Boolean = this is Spot
}

@Composable
fun WalkieBottomNavigation(
    navController: NavController,
    items: List<BottomNavItem> = listOf(
        BottomNavItem.Home,
        BottomNavItem.Spot,
        BottomNavItem.MyPage
    ),
    onItemClick: (BottomNavItem) -> Unit = { item ->
        navController.navigate(item.route) {
            navController.graph.startDestinationRoute?.let { route ->
                popUpTo(route) {
                    saveState = true
                }
            }
            launchSingleTop = true
            restoreState = true
        }
    },
    onCenterItemClick: () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(62.5.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .align(Alignment.BottomCenter),
            color = WalkieTheme.colors.gray50,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            border = BorderStroke(3.5.dp, WalkieTheme.colors.gray50)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    CreateTabItem(
                        item = items.first { it !is BottomNavItem.Spot },
                        currentRoute = currentRoute,
                        onItemClick = onItemClick
                    )
                }

                Box(modifier = Modifier.weight(1f)) {
                    // 여기는 비워둠 (중앙 아이템이 위에 올라갈 것임)
                }

                Box(modifier = Modifier.weight(1f)) {
                    CreateTabItem(
                        item = items.last { it !is BottomNavItem.Spot },
                        currentRoute = currentRoute,
                        onItemClick = onItemClick
                    )
                }
            }
        }

        items.find { it.isCenterItem() }?.let { centerItem ->
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .align(Alignment.BottomCenter)
                    .offset(y = (-6.5).dp),
                contentAlignment = Alignment.Center
            ) {
                // (테두리 역할)
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(WalkieTheme.colors.gray50, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(49.dp)
                            .background(WalkieTheme.colors.blue300, CircleShape)
                            .noRippleClickable { onCenterItemClick.invoke() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = centerItem.icon),
                            contentDescription = null,
                            tint = WalkieTheme.colors.white,
                            modifier = Modifier
                                .size(28.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CreateTabItem(
    item: BottomNavItem,
    currentRoute: String?,
    onItemClick: (BottomNavItem) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable { onItemClick(item) }
            .padding(vertical = 6.dp)
    ) {
        val iconRes = if (currentRoute == item.route) {
            when (item) {
                is BottomNavItem.Home -> R.drawable.ic_home_on
                is BottomNavItem.MyPage -> R.drawable.ic_mypage_on
                else -> item.icon
            }
        } else {
            item.icon
        }

        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = if (currentRoute == item.route) WalkieTheme.colors.gray700 else WalkieTheme.colors.gray400,
            modifier = Modifier.size(24.dp)
        )

        Box(
            modifier = Modifier.height(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(item.label),
                style = WalkieTheme.typography.caption2,
                color = if (currentRoute == item.route) WalkieTheme.colors.gray700 else WalkieTheme.colors.gray400,
                maxLines = 1,
                overflow = TextOverflow.Visible
            )
        }
    }
}