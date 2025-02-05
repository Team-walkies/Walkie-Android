package com.startup.data.di

import com.startup.data.BuildConfig
import com.startup.data.annotations.AuthHttpClient
import com.startup.data.annotations.AuthRetrofit
import com.startup.data.annotations.CommonHttpClient
import com.startup.data.annotations.CommonRetrofit
import com.startup.data.remote.service.TempService
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
    fun providesConvertorFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    @CommonHttpClient
    fun providesCommonHttpClient(
        /*headerInterceptor: Interceptor, TODO 나중에 추가 될 예정 */
        loggerInterceptor: HttpLoggingInterceptor,
    ) =
        OkHttpClient.Builder()
            .addInterceptor(loggerInterceptor)
            .build()


    @Provides
    @Singleton
    @AuthHttpClient
    fun providesAuthHttpClient(
        /*headerInterceptor: Interceptor, TODO 나중에 추가 될 예정 */
        loggerInterceptor: HttpLoggingInterceptor,
    ) =
        OkHttpClient.Builder()
//            .addInterceptor(headerInterceptor)
            .addInterceptor(loggerInterceptor)
            .build()


    @Provides
    @Singleton
    @CommonRetrofit
    fun providesCommonRetrofit(
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
    fun provideTempService(
        @AuthRetrofit retrofit: Retrofit,
    ): TempService = retrofit.create(TempService::class.java)

}