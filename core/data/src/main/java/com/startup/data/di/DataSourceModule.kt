package com.startup.data.di

import com.startup.data.datasource.AuthDataSource
import com.startup.data.datasource.CharacterDataSource
import com.startup.data.datasource.EggDataSource
import com.startup.data.datasource.MemberDataSource
import com.startup.data.datasource.NoticeDataSource
import com.startup.data.datasource.ReviewDataSource
import com.startup.data.datasource.SpotDataSource
import com.startup.data.datasource.Temp2DataSource
import com.startup.data.datasource.TempDataSource
import com.startup.data.datasource.UserDataSource
import com.startup.data.local.datasourceimpl.Temp2DataSourceImpl
import com.startup.data.local.datasourceimpl.UserDataSourceImpl
import com.startup.data.remote.datasourceimpl.AuthDataSourceImpl
import com.startup.data.remote.datasourceimpl.CharacterDataSourceImpl
import com.startup.data.remote.datasourceimpl.EggDataSourceImpl
import com.startup.data.remote.datasourceimpl.MemberDataSourceImpl
import com.startup.data.remote.datasourceimpl.NoticeDataSourceImpl
import com.startup.data.remote.datasourceimpl.ReviewDataSourceImpl
import com.startup.data.remote.datasourceimpl.SpotDataSourceImpl
import com.startup.data.remote.datasourceimpl.TempDataSourceImpl
import com.startup.data.util.NetworkChecker
import com.startup.data.util.NetworkCheckerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindsTempDataSource(dataSource: TempDataSourceImpl): TempDataSource

    @Singleton
    @Binds
    abstract fun bindsTemp2DataSource(dataSource: Temp2DataSourceImpl): Temp2DataSource

    @Singleton
    @Binds
    abstract fun bindsCharacterDataSource(dataSource: CharacterDataSourceImpl): CharacterDataSource

    @Singleton
    @Binds
    abstract fun bindsEggDataSource(dataSource: EggDataSourceImpl): EggDataSource

    @Singleton
    @Binds
    abstract fun bindsMemberDataSource(dataSource: MemberDataSourceImpl): MemberDataSource

    @Singleton
    @Binds
    abstract fun bindsNoticeDataSource(dataSource: NoticeDataSourceImpl): NoticeDataSource

    @Singleton
    @Binds
    abstract fun bindsReviewDataSource(dataSource: ReviewDataSourceImpl): ReviewDataSource

    @Singleton
    @Binds
    abstract fun bindsSpotDataSource(dataSource: SpotDataSourceImpl): SpotDataSource

    @Singleton
    @Binds
    abstract fun bindsAuthDataSource(dataSource: AuthDataSourceImpl): AuthDataSource

    @Singleton
    @Binds
    abstract fun bindsUserDataSource(dataSource: UserDataSourceImpl): UserDataSource

    @Singleton
    @Binds
    abstract fun bindsNetworkChecker(networkChecker: NetworkCheckerImpl): NetworkChecker
}
