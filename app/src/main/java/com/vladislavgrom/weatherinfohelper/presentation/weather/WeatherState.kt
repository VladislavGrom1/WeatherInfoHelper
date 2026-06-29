package com.vladislavgrom.weatherinfohelper.presentation.weather

import com.vladislavgrom.weatherinfohelper.domain.weather.model.WeatherInfo

sealed interface WeatherState{
    data object Initial: WeatherState
    data object RequestingPermissions: WeatherState
    data object DataLoading: WeatherState
    data class DataLoaded(
        val weatherData: WeatherInfo,
        val latitude: Double?,
        val longitude: Double?,
    ) : WeatherState
}