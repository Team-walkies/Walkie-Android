package com.startup.design_system.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Immutable
data class WalkieTypography(
    val head1: TextStyle,
    val head2: TextStyle,
    val head3: TextStyle,
    val head4: TextStyle,
    val head5: TextStyle,
    val head6: TextStyle,
    val body1: TextStyle,
    val body2: TextStyle,
    val caption1: TextStyle,
    val caption2: TextStyle,
)

val LocalWalkieTypography = staticCompositionLocalOf {
    WalkieTypography(
        head1 = TextStyle.Default,
        head2 = TextStyle.Default,
        head3 = TextStyle.Default,
        head4 = TextStyle.Default,
        head5 = TextStyle.Default,
        head6 = TextStyle.Default,
        body1 = TextStyle.Default,
        body2 = TextStyle.Default,
        caption1 = TextStyle.Default,
        caption2 = TextStyle.Default,
    )
}

@Composable
fun getWalkieTypography(): WalkieTypography =
    WalkieTypography(
        head1 = TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 48.dp.toSp(),
            lineHeight = 58.dp.toSp(),
            platformStyle = PlatformTextStyle(includeFontPadding = false),
        ),
        head2 = TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = 24.dp.toSp(),
            lineHeight = 34.dp.toSp(),
            platformStyle = PlatformTextStyle(includeFontPadding = false),
        ),
        head3 = TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = 20.dp.toSp(),
            lineHeight = 30.dp.toSp(),
            platformStyle = PlatformTextStyle(includeFontPadding = false),
        ),
        head4 = TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = 18.dp.toSp(),
            lineHeight = 28.dp.toSp(),
            platformStyle = PlatformTextStyle(includeFontPadding = false),
        ),
        head5 = TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = 16.dp.toSp(),
            lineHeight = 24.dp.toSp(),
            platformStyle = PlatformTextStyle(includeFontPadding = false),
        ),
        head6 = TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = 14.dp.toSp(),
            lineHeight = 20.dp.toSp(),
            platformStyle = PlatformTextStyle(includeFontPadding = false),
        ),
        body1 = TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Medium,
            fontSize = 16.dp.toSp(),
            lineHeight = 24.dp.toSp(),
            platformStyle = PlatformTextStyle(includeFontPadding = false),
        ),
        body2 = TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Medium,
            fontSize = 14.dp.toSp(),
            lineHeight = 20.dp.toSp(),
            platformStyle = PlatformTextStyle(includeFontPadding = false),
        ),
        caption1 = TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Medium,
            fontSize = 12.dp.toSp(),
            lineHeight = 16.dp.toSp(),
            platformStyle = PlatformTextStyle(includeFontPadding = false),
        ),
        caption2 = TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Medium,
            fontSize = 10.dp.toSp(),
            lineHeight = 12.dp.toSp(),
            platformStyle = PlatformTextStyle(includeFontPadding = false),
        )
    )