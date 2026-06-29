package com.vladislavgrom.weatherinfohelper.domain.weather.repository
import com.vladislavgrom.weatherinfohelper.domain.weather.model.WeatherInfo


interface WeatherRepository {
    suspend fun getWeatherData(latitude: Double, longitude: Double) : WeatherInfo
}