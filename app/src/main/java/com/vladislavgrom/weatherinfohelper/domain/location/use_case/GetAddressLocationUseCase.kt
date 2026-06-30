package com.vladislavgrom.weatherinfohelper.domain.location.use_case

import android.location.Location
import com.vladislavgrom.weatherinfohelper.domain.location.repository.LocationRepository
import javax.inject.Inject

class GetAddressLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend fun call(latitude: Double, longitude: Double) : String {
        return locationRepository.getAddressLocation(latitude = latitude, longitude = longitude)
    }
}