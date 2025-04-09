package com.startup.data.local.datasourceimpl

import com.startup.common.provider.LocationProvider
import com.startup.data.datasource.LocationDataSource
import com.startup.domain.model.location.LocationData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationDataSourceImpl @Inject constructor(
    private val locationProvider: LocationProvider
) : LocationDataSource {
    override fun getCurrentLocation(): Flow<LocationData> {
        return locationProvider.getCurrentLocation()
    }
}