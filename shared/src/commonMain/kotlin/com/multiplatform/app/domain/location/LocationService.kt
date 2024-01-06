package com.multiplatform.app.domain.location

interface LocationService {
    suspend fun getCurrentLocation(): DeviceLocation
}
