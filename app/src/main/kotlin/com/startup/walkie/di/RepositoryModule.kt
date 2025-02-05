package com.startup.walkie.di

import com.startup.data.repository.TempRepositoryImpl
import com.startup.domain.repository.TempRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsTempRepository(tempRepository: TempRepositoryImpl): TempRepository
}