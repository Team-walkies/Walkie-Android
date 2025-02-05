package com.startup.data.remote.service

import retrofit2.http.GET

internal interface TempService {

    @GET("/temp")
    suspend fun getData(): Unit
}