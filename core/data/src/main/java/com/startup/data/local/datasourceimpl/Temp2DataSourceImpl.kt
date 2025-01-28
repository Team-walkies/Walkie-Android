package com.startup.data.local.datasourceimpl

import com.startup.data.datasource.Temp2DataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class Temp2DataSourceImpl @Inject constructor() : Temp2DataSource {
    override fun getData(): Flow<Unit> = flow {
        emit(Unit)
    }
}