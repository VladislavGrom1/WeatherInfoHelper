package com.vladislavgrom.weatherinfohelper.data.weather.model

import com.squareup.moshi.Json

data class WeatherDto (
    @field:Json(name = "hourly")
    val hourly: WeatherDataDto
)