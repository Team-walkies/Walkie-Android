package com.startup.common.provider

import android.content.Context
import android.location.LocationManager
import com.startup.common.util.UsePermissionHelper
import com.startup.domain.model.location.LocationData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LocationProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : LocationProvider {
    override fun getCurrentLocation(): Flow<LocationData> = flow {
        // 위치 권한 확인
        val hasForegroundLocationPermission = UsePermissionHelper.isGrantedPermissions(
            context = context,
            permissions = UsePermissionHelper.getTypeOfPermission(UsePermissionHelper.Permission.FOREGROUND_LOCATION)
        )

        if (!hasForegroundLocationPermission) {
            emit(LocationData(latitude = null, longitude = null))
            return@flow
        }

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // 위치 서비스 활성화 여부 확인
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        // GPS 또는 네트워크 위치 가져오기 시도
        val location = when {
            isGpsEnabled -> locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            isNetworkEnabled -> locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            else -> null
        }

        emit(
            LocationData(
                latitude = location?.latitude,
                longitude = location?.longitude
            )
        )
    }.flowOn(Dispatchers.IO)
}