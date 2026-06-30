package com.vladislavgrom.weatherinfohelper.data.weather.repository

import com.vladislavgrom.weatherinfohelper.data.weather.data_source.WeatherRemoteDataSource
import com.vladislavgrom.weatherinfohelper.data.weather.mappers.toWeatherInfo
import com.vladislavgrom.weatherinfohelper.domain.util.Resource
import com.vladislavgrom.weatherinfohelper.domain.weather.model.WeatherInfo
import com.vladislavgrom.weatherinfohelper.domain.weather.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherRemoteDataSource: WeatherRemoteDataSource
) : WeatherRepository {

    override suspend fun getWeatherData(latitude: Double, longitude: Double): Resource<WeatherInfo> {
        return try {
            val result = weatherRemoteDataSource.getWeatherData(latitude, longitude).toWeatherInfo()
            Resource.Success(result)
        } catch (e: Exception) {
            Resource.Error("Ошибка: ${e.message}")
        }
    }
}