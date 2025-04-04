package com.startup.domain.repository

import com.startup.domain.model.location.LocationData
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getCurrentLocation(): Flow<LocationData>
}
