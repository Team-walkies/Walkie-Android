package com.startup.data.datasource

import kotlinx.coroutines.flow.Flow

interface Temp2DataSource {
    fun getData(): Flow<Unit>
}