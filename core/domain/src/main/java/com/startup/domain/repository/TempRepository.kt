package com.startup.domain.repository

import kotlinx.coroutines.flow.Flow

interface TempRepository {
    fun getData(): Flow<Unit>
}