package com.startup.data.remote.datasourceimpl

import com.startup.data.datasource.TempDataSource
import com.startup.data.remote.service.TempService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class TempDataSourceImpl @Inject constructor(private val tempService: TempService) : TempDataSource {
    override fun getData(): Flow<Unit> = flow {
        emit(tempService.getData())
    }
}