package com.vladislavgrom.weatherinfohelper.domain.map.repository

import com.vladislavgrom.weatherinfohelper.domain.map.model.MapPlaceInfo
import com.yandex.mapkit.search.PlaceInfo

interface MapRepository {
    suspend fun searchPlaces(query: String, latitude: Double, longitude: Double): List<MapPlaceInfo>
}