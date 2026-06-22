package com.vladislavgrom.weatherinfohelper.domain.weather.repository

import com.vladislavgrom.weatherinfohelper.data.weather.model.WeatherDto
import com.vladislavgrom.weatherinfohelper.domain.weather.model.WeatherInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherRepository {
    suspend fun getWeatherData(latitude: Double, longitude: Double) : WeatherInfo
}