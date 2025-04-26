package com.startup.home.navigation

sealed class HomeScreenNav(val route: String) {
    data object GainEgg : HomeScreenNav("gainEgg")
    data object GainCharacter : HomeScreenNav("gainCharacter")
    data object SpotArchive : HomeScreenNav("spotArchive")
    data object Notification : HomeScreenNav("notification")
}

sealed class MyPageScreenNav(val route: String) {
    data object MyInfo : MyPageScreenNav("myInfo")
    data object PushSetting : MyPageScreenNav("pushSetting")
    data object Notice : MyPageScreenNav("notice")
    data object PersonalInfoPolicy : MyPageScreenNav("personalInfoPolicy")
    data object ServiceTerm : MyPageScreenNav("serviceTerm")
    data object RequestUserOpinion : MyPageScreenNav("requestUserOpinion")
    data object Notification : MyPageScreenNav("notification")
    data object Unlink : MyPageScreenNav("unlink")
}

sealed class MainScreenNav(val route: String) {
    data object BottomNavigation : MainScreenNav("main")
    data object HomeGraph : MainScreenNav("homeGraph")
    data object MyPageGraph : MainScreenNav("myPageGraph")
}

sealed class GainEggScreenNav(val route: String) {
    data object GainEggNav : GainEggScreenNav("gainEgg")
    data object EggGainProbabilityNav : GainEggScreenNav("eggGainProbability")
}
sealed class SpotArchiveScreenNav(val route: String) {
    data object SpotArchive : SpotArchiveScreenNav("spotArchive")
    data object SpotReviewModify : SpotArchiveScreenNav("spotReviewModify")
}
sealed class NoticeScreenNav(val route: String) {
    data object NoticeList : NoticeScreenNav("noticeList")
    data object NoticeDetail : NoticeScreenNav("noticeDetail")
}