package com.startup.data.repository

import com.startup.data.datasource.Temp2DataSource
import com.startup.data.datasource.TempDataSource
import com.startup.domain.repository.TempRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class TempRepositoryImpl @Inject constructor(
    private val tempDataSource: TempDataSource,
    private val temp2DataSource: Temp2DataSource,
) : TempRepository {
    override fun getData(): Flow<Unit> {
        tempDataSource.getData()
        return temp2DataSource.getData()
    }
}