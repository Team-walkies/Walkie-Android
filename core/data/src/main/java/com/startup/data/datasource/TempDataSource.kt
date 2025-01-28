package com.startup.data.datasource

import kotlinx.coroutines.flow.Flow

interface TempDataSource {
    fun getData(): Flow<Unit>
}