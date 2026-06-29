package com.vladislavgrom.weatherinfohelper.domain.location.repository

import android.location.Location

interface LocationRepository {
    suspend fun getCurrentLocation(): Location?
}