package com.vladislavgrom.weatherinfohelper.data.weather.data_source

import com.vladislavgrom.weatherinfohelper.common.api.WeatherApi
import com.vladislavgrom.weatherinfohelper.data.weather.model.WeatherDto
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

class WeatherRemoteDataSource @Inject constructor(
    private val weatherApi: WeatherApi
)  {
    suspend fun getWeatherData(latitude: Double, longitude: Double) : WeatherDto {
        return weatherApi.getWeatherData(latitude, longitude)
    }
}