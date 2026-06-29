package com.vladislavgrom.weatherinfohelper.common.location

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Location
import android.location.LocationManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.vladislavgrom.weatherinfohelper.common.permission.PermissionChecker
import com.vladislavgrom.weatherinfohelper.domain.location.repository.LocationRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class LocationRepositoryImpl @Inject constructor(
    private val locationClient: FusedLocationProviderClient,
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
}