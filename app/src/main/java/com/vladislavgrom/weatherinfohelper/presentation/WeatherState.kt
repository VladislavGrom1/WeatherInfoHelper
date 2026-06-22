package com.vladislavgrom.weatherinfohelper.presentation

import com.vladislavgrom.weatherinfohelper.domain.weather.model.WeatherInfo

data class WeatherState (
    val weatherData: WeatherInfo? = null
)