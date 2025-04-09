package com.startup.data.datasource

import com.startup.domain.model.location.LocationData
import kotlinx.coroutines.flow.Flow

interface LocationDataSource {
    fun getCurrentLocation(): Flow<LocationData>
}
