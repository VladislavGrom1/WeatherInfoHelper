package com.vladislavgrom.weatherinfohelper.domain.map.use_case

import com.vladislavgrom.weatherinfohelper.domain.map.model.MapPlaceInfo
import com.vladislavgrom.weatherinfohelper.domain.map.repository.MapRepository
import javax.inject.Inject

class SearchPlacesUseCase @Inject constructor(
    private val mapRepository: MapRepository
) {
    suspend fun call(query: String, latitude: Double, longitude: Double): List<MapPlaceInfo> =
        mapRepository.searchPlaces(query, latitude, longitude)
}