package com.vladislavgrom.weatherinfohelper.common.location

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.vladislavgrom.weatherinfohelper.common.permission.PermissionChecker
import com.vladislavgrom.weatherinfohelper.domain.location.repository.LocationRepository
import dev.jordond.compass.geocoder.Geocoder
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class LocationRepositoryImpl @Inject constructor(
    private val locationClient: FusedLocationProviderClient,
    private val geocoder: Geocoder,
    private val application: Application,
    private val permissionChecker: PermissionChecker
) : LocationRepository {

    override suspend fun getCurrentLocation(): Location? {
        val hasLocationPermission = permissionChecker.hasLocationPermission()

        val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.GPS_PROVIDER)

        if (!hasLocationPermission || !isGpsEnabled) {
            return null
        }

        @SuppressLint("MissingPermission")
        return suspendCancellableCoroutine { continuation ->
            locationClient.lastLocation.apply {
                if(isComplete){
                    if(isSuccessful){
                        continuation.resume(result)
                    } else{
                        continuation.resume(null)
                    }
                    return@suspendCancellableCoroutine
                }
                addOnSuccessListener {
                    continuation.resume(it)
                }
                addOnFailureListener{
                    continuation.resume(null)
                }
                addOnCanceledListener {
                    continuation.cancel()
                }
            }
        }
    }

    override suspend fun getAddressLocation(latitude: Double, longitude: Double): String {
        val result = geocoder.reverse(
            latitude = latitude,
            longitude = longitude
        )
        val firstPlace = result.getFirstOrNull()
        val location = firstPlace?.let {
            "${it.locality ?: "Unknown city"}, ${it.country ?: "Unknown country"}"
        } ?: "Location not found"

        Log.d("GeocoderDebug", "Result: $result")
        return location
    }
}