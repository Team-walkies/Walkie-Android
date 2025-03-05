package com.startup.data.annotations

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserTokenDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserSettingDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserNotificationDataStore
