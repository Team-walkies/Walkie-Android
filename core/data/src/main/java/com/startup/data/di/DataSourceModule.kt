package com.startup.data.di

import com.startup.data.datasource.Temp2DataSource
import com.startup.data.datasource.TempDataSource
import com.startup.data.local.datasourceimpl.Temp2DataSourceImpl
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
    abstract fun bindsTempDataSource(tempDataSource: TempDataSourceImpl): TempDataSource

    @Singleton
    @Binds
    abstract fun bindsTemp2DataSource(tempDataSource: Temp2DataSourceImpl): Temp2DataSource

    @Singleton
    @Binds
    abstract fun bindsNetworkChecker(networkChecker: NetworkCheckerImpl): NetworkChecker
}
