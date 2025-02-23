package com.startup.ui

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/*
    GrayScale
    워키 서비스의 전반적인 UI를 구성하는 Base Color Scale
 */
val White = Color(0xFFFFFFFF)
val Gray50 = Color(0xFFF9F8FA)
val Gray100 = Color(0xFFF3F4F6)
val Gray200 = Color(0xFFE5E7EB)
val Gray300 = Color(0xFFCDCFD4)
val Gray400 = Color(0xFF9CA1AB)
val Gray500 = Color(0xFF737882)
val Gray600 = Color(0xFF545963)
val GRAY700 = Color(0xFF383C44)
val Gray800 = Color(0xFF2B2E36)
val Gray900 = Color(0xFF1E2127)
val Gray900Opacity70 = Color(0xB21E2127)
val Black = Color(0xFF000000)

/*
    Blue
    워키의 전반적인 UI를 구성하는 Blue Color Scale
 */
val Blue30 = Color(0xFFF0F8FD)
val Blue50 = Color(0xFFEAF4FB)
val Blue100 = Color(0xFFB7E5FF)
val Blue200 = Color(0xFF70CFFF)
val Blue300 = Color(0xFF0DAEFF)
val Blue300Opacity10 = Color(0x1A0DAEFF)  // opacity 10%
val Blue400 = Color(0xFF0091DA)
val Blue500 = Color(0xFF00839A) // normal

/*
    Red
    대체로 warning, notification에 쓰이는 컬러
 */
val Red50 = Color(0xFFF5D0D0)
val Red100 = Color(0xFFFF6D6D)

/*
    Orange
    에픽 등급에 쓰이는 컬러
 */
val Orange100 = Color(0xFFF9AF37)
val Orange300 = Color(0xFFD28800)  // epic

/*
    Yellow
    카카오 로그인 버튼에 쓰이는 컬러
 */
val Yellow100 = Color(0xFFFEE500)
val Yellow200 = Color(0xFFF0C507)

/*
    Green
    희귀 등급에 쓰이는 컬러
 */
val Green50 = Color(0xFFB2EDD3)
val Green100 = Color(0xFF5FCC9B)
val Green200 = Color(0xFF49C06B)
val Green300 = Color(0xFF00942A)  // rare

/*
    Purple
    전설 등급에 쓰이는 컬러
 */
val Purple100 = Color(0xFF9393ED)
val Purple200 = Color(0xFF6B6BE0)
val Purple300 = Color(0xFF5309A8)  // legendary

@Immutable
data class WalkieSemanticColor(
    val white: Color,
    val gray50: Color,
    val gray100: Color,
    val gray200: Color,
    val gray300: Color,
    val gray400: Color,
    val gray500: Color,
    val gray600: Color,
    val gray700: Color,
    val gray800: Color,
    val gray900: Color,
    val gray900Opacity70: Color,
    val black: Color,

    val blue30: Color,
    val blue50: Color,
    val blue100: Color,
    val blue200: Color,
    val blue300: Color,
    val blue300Opacity10: Color,
    val blue400: Color,
    val blue500: Color,

    val red50: Color,
    val red100: Color,

    val orange100: Color,
    val orange300: Color,

    val yellow100: Color,
    val yellow200: Color,

    val green50: Color,
    val green100: Color,
    val green200: Color,
    val green300: Color, // rare

    val purple100: Color,
    val purple200: Color,
    val purple300: Color,
)

val LocalWalkieColors = staticCompositionLocalOf {
    WalkieSemanticColor(
        white = Color.Unspecified,
        gray50 = Color.Unspecified,
        gray100 = Color.Unspecified,
        gray200 = Color.Unspecified,
        gray300 = Color.Unspecified,
        gray400 = Color.Unspecified,
        gray500 = Color.Unspecified,
        gray600 = Color.Unspecified,
        gray700 = Color.Unspecified,
        gray800 = Color.Unspecified,
        gray900 = Color.Unspecified,
        gray900Opacity70 = Color.Unspecified,
        black = Color.Unspecified,

        blue30 = Color.Unspecified,
        blue50 = Color.Unspecified,
        blue100 = Color.Unspecified,
        blue200 = Color.Unspecified,
        blue300 = Color.Unspecified,
        blue300Opacity10 = Color.Unspecified,
        blue400 = Color.Unspecified,
        blue500 = Color.Unspecified,

        red50 = Color.Unspecified,
        red100 = Color.Unspecified,

        orange100 = Color.Unspecified,
        orange300 = Color.Unspecified,

        yellow100 = Color.Unspecified,
        yellow200 = Color.Unspecified,

        green50 = Color.Unspecified,
        green100 = Color.Unspecified,
        green200 = Color.Unspecified,
        green300 = Color.Unspecified,

        purple100 = Color.Unspecified,
        purple200 = Color.Unspecified,
        purple300 = Color.Unspecified,
    )
}

val LocalWalkieLightColorScheme = WalkieSemanticColor(
    white = White,
    gray50 = Gray50,
    gray100 = Gray100,
    gray200 = Gray200,
    gray300 = Gray300,
    gray400 = Gray400,
    gray500 = Gray500,
    gray600 = Gray600,
    gray700 = GRAY700,
    gray800 = Gray800,
    gray900 = Gray900,
    gray900Opacity70 = Gray900Opacity70,
    black = Black,

    blue30 = Blue30,
    blue50 = Blue50,
    blue100 = Blue100,
    blue200 = Blue200,
    blue300 = Blue300,
    blue300Opacity10 = Blue300Opacity10,
    blue400 = Blue400,
    blue500 = Blue500,

    red50 = Red50,
    red100 = Red100,

    orange100 = Orange100,
    orange300 = Orange300,

    yellow100 = Yellow100,
    yellow200 = Yellow200,

    green50 = Green50,
    green100 = Green100,
    green200 = Green200,
    green300 = Green300,

    purple100 = Purple100,
    purple200 = Purple200,
    purple300 = Purple300,
)

val LocalWalkieDarkColorScheme = WalkieSemanticColor(
    white = Black, // 다크 테마에서는 반전
    gray50 = Gray900,
    gray100 = Gray800,
    gray200 = GRAY700,
    gray300 = Gray600,
    gray400 = Gray500,
    gray500 = Gray400,
    gray600 = Gray300,
    gray700 = Gray200,
    gray800 = Gray100,
    gray900 = Gray50,
    gray900Opacity70 = Gray900Opacity70,
    black = White, // 다크 테마에서는 반전

    blue30 = Blue30,
    blue50 = Blue50,
    blue100 = Blue100,
    blue200 = Blue200,
    blue300 = Blue300,
    blue300Opacity10 = Blue300Opacity10,
    blue400 = Blue400,
    blue500 = Blue500,

    red50 = Red50,
    red100 = Red100,

    orange100 = Orange100,
    orange300 = Orange300,

    yellow100 = Yellow100,
    yellow200 = Yellow200,

    green50 = Green50,
    green100 = Green100,
    green200 = Green200,
    green300 = Green300,

    purple100 = Purple100,
    purple200 = Purple200,
    purple300 = Purple300,
)