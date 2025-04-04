package com.startup.data.repository

import com.startup.data.datasource.LocationDataSource
import com.startup.domain.model.location.LocationData
import com.startup.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationDataSource: LocationDataSource
) : LocationRepository {

    override fun getCurrentLocation(): Flow<LocationData> {
        return locationDataSource.getCurrentLocation()
    }
}