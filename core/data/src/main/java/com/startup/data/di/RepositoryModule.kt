package com.startup.data.di

import com.startup.data.repository.AuthRepositoryImpl
import com.startup.data.repository.CharacterRepositoryImpl
import com.startup.data.repository.EggRepositoryImpl
import com.startup.data.repository.LocationRepositoryImpl
import com.startup.data.repository.MemberRepositoryImpl
import com.startup.data.repository.NoticeRepositoryImpl
import com.startup.data.repository.SpotRepositoryImpl
import com.startup.data.repository.UserRepositoryImpl
import com.startup.domain.repository.AuthRepository
import com.startup.domain.repository.CharacterRepository
import com.startup.domain.repository.EggRepository
import com.startup.domain.repository.LocationRepository
import com.startup.domain.repository.MemberRepository
import com.startup.domain.repository.NoticeRepository
import com.startup.domain.repository.SpotRepository
import com.startup.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsAuthRepository(repository: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindsCharacterRepository(repository: CharacterRepositoryImpl): CharacterRepository

    @Binds
    @Singleton
    abstract fun bindsAuthEggRepository(repository: EggRepositoryImpl): EggRepository

    @Binds
    @Singleton
    abstract fun bindsMemberRepository(repository: MemberRepositoryImpl): MemberRepository

    @Binds
    @Singleton
    abstract fun bindsNoticeRepository(repository: NoticeRepositoryImpl): NoticeRepository

    @Binds
    @Singleton
    abstract fun bindsSpotRepository(repository: SpotRepositoryImpl): SpotRepository

    @Binds
    @Singleton
    abstract fun bindsUserRepository(repository: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindLocationRepository(repository: LocationRepositoryImpl): LocationRepository
}