package com.vladislavgrom.weatherinfohelper.data.map.mappers

import com.vladislavgrom.weatherinfohelper.domain.map.model.MapPlaceInfo
import com.yandex.mapkit.GeoObject

fun GeoObject.toMapPlaceInfo(): MapPlaceInfo{
    val point = geometry.firstOrNull()?.point
    return MapPlaceInfo(
        name = name.orEmpty(),
        latitude = point?.latitude ?: 0.0,
        longitude = point?.longitude ?: 0.0
    )
}