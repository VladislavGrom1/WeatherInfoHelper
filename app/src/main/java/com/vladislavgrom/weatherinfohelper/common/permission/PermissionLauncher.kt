package com.vladislavgrom.weatherinfohelper.common.permission

import android.Manifest
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts

class PermissionLauncher(
    private val activity: ComponentActivity,
    private val onResult: (Boolean) -> Unit
) {

    private val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val launcher =
        activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            onResult(result.values.any { it })
        }

    fun requestPermissions() {
        launcher.launch(locationPermissions)
    }
}