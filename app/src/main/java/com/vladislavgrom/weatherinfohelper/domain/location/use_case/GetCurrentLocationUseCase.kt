package com.vladislavgrom.weatherinfohelper.domain.location.use_case

import android.location.Location
import com.vladislavgrom.weatherinfohelper.domain.location.repository.LocationRepository
import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend fun call() : Location? {
        return locationRepository.getCurrentLocation()
    }
}