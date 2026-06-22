package com.vladislavgrom.weatherinfohelper.domain.weather.model

import android.icu.util.LocaleData
import java.time.LocalDateTime

data class WeatherData (
    val time: LocalDateTime,
    val temperatureC: Double,
    val pressure: Double,
    val windSpeed: Double,
    val humidity: Double,
    val weatherType: WeatherType
)