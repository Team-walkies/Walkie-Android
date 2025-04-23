package com.startup.login.navigation

sealed class LoginScreenNav(val route: String) {
    data object Onboarding : LoginScreenNav("onboarding")
    data object NickNameSetting : LoginScreenNav("nicknameSetting")
    data object GetCharacter : LoginScreenNav("getCharacter")
}