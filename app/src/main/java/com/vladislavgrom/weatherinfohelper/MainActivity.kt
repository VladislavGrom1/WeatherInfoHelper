package com.vladislavgrom.weatherinfohelper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.vladislavgrom.weatherinfohelper.common.permission.PermissionChecker
import com.vladislavgrom.weatherinfohelper.common.permission.PermissionLauncher
import com.vladislavgrom.weatherinfohelper.presentation.weather.WeatherScreen
import com.vladislavgrom.weatherinfohelper.presentation.weather.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val weatherViewModel: WeatherViewModel by viewModels()

    @Inject
    lateinit var permissionChecker: PermissionChecker

    private lateinit var permissionLauncher: PermissionLauncher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionLauncher = PermissionLauncher(this) { granted ->
            weatherViewModel.onPermissionResult(granted)
        }

        setContent {
            WeatherScreen(weatherViewModel)
        }

        if (permissionChecker.hasLocationPermission()) {
            weatherViewModel.onPermissionResultAlreadyGranted()
        } else {
            weatherViewModel.showPermissionRequest()
            permissionLauncher.requestPermissions()
        }

    }
}
