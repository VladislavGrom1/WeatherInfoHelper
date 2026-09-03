package com.vladislavgrom.weatherinfohelper.data.map.repository

import com.vladislavgrom.weatherinfohelper.data.map.mappers.toMapPlaceInfo
import com.vladislavgrom.weatherinfohelper.domain.map.model.MapPlaceInfo
import com.vladislavgrom.weatherinfohelper.domain.map.repository.MapRepository
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.SearchManager
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.Session
import com.yandex.mapkit.search.Response
import com.yandex.runtime.Error as MapKitError
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class MapRepositoryImpl @Inject constructor(
    private val searchManager: SearchManager
) : MapRepository {

    override suspend fun searchPlaces(
        query: String,
        latitude: Double,
        longitude: Double
    ): List<MapPlaceInfo> = suspendCancellableCoroutine { cont ->
        val listener = object : Session.SearchListener {
            override fun onSearchResponse(response: Response) {
                val places = response.collection.children
                    .mapNotNull { it.obj }
                    .map { it.toMapPlaceInfo() }
                if (cont.isActive) cont.resume(places)
            }

            override fun onSearchError(error: MapKitError) {
                if (cont.isActive) {
                    cont.resumeWithException(Exception("Ошибка поиска мест: $error"))
                }
            }
        }

        val session = searchManager.submit(
            query,
            Geometry.fromPoint(Point(latitude, longitude)),
            SearchOptions(),
            listener
        )

        cont.invokeOnCancellation { session.cancel() }
    }
}