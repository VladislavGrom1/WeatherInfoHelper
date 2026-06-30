package com.vladislavgrom.weatherinfohelper.domain.location.repository

import android.location.Location

interface LocationRepository {
    suspend fun getCurrentLocation(): Location?
    suspend fun getAddressLocation(latitude: Double, longitude: Double): String
}