package com.startup.common.provider

import com.startup.domain.model.location.LocationData
import kotlinx.coroutines.flow.Flow

interface LocationProvider {
    fun getCurrentLocation(): Flow<LocationData>
}