package com.startup.data.di

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.startup.data.BuildConfig
import com.startup.data.annotations.AuthHttpClient
import com.startup.data.annotations.AuthRetrofit
import com.startup.data.annotations.CommonHttpClient
import com.startup.data.annotations.CommonRetrofit
import com.startup.data.remote.service.AuthService
import com.startup.data.remote.service.CharacterService
import com.startup.data.remote.service.EggService
import com.startup.data.remote.service.MemberService
import com.startup.data.remote.service.NoticeService
import com.startup.data.remote.service.ReviewService
import com.startup.data.remote.service.SpotService
import com.startup.data.util.AuthInterceptor
import com.startup.data.util.TokenAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun providesLoggerInterceptor() = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    @Provides
    @Singleton
    fun provideRemoteConfig() = Firebase.remoteConfig

    @Provides
    @Singleton
    fun providesConvertorFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    @CommonHttpClient
    fun providesCommonHttpClient(
        loggerInterceptor: HttpLoggingInterceptor,
    ) =
        OkHttpClient.Builder()
            .addInterceptor(loggerInterceptor)
            .build()

    @Provides
    @Singleton
    @CommonRetrofit
    fun providesCommonRetrofit(
        @CommonHttpClient okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory,
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()

    @Provides
    @Singleton
    fun provideLoginService(
        @CommonRetrofit retrofit: Retrofit,
    ): AuthService = retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    @AuthHttpClient
    fun providesAuthHttpClient(
        tokenAuthenticator: TokenAuthenticator,
        headerInterceptor: AuthInterceptor,
        loggerInterceptor: HttpLoggingInterceptor,
    ) =
        OkHttpClient.Builder()
            .authenticator(tokenAuthenticator)
            .addInterceptor(headerInterceptor)
            .addInterceptor(loggerInterceptor)
            .build()

    @Provides
    @Singleton
    @AuthRetrofit
    fun providesAuthRetrofit(
        @AuthHttpClient okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory,
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()

    @Provides
    @Singleton
    fun provideCharacterService(
        @AuthRetrofit retrofit: Retrofit,
    ): CharacterService = retrofit.create(CharacterService::class.java)

    @Provides
    @Singleton
    fun provideEggService(
        @AuthRetrofit retrofit: Retrofit,
    ): EggService = retrofit.create(EggService::class.java)

    @Provides
    @Singleton
    fun provideMemberService(
        @AuthRetrofit retrofit: Retrofit,
    ): MemberService = retrofit.create(MemberService::class.java)

    @Provides
    @Singleton
    fun provideNoticeService(
        @AuthRetrofit retrofit: Retrofit,
    ): NoticeService = retrofit.create(NoticeService::class.java)

    @Provides
    @Singleton
    fun provideReviewService(
        @AuthRetrofit retrofit: Retrofit,
    ): ReviewService = retrofit.create(ReviewService::class.java)

    @Provides
    @Singleton
    fun provideSpotService(
        @AuthRetrofit retrofit: Retrofit,
    ): SpotService = retrofit.create(SpotService::class.java)
}