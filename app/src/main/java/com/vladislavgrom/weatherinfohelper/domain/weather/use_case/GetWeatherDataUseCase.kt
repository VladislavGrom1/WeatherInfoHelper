package com.vladislavgrom.weatherinfohelper.domain.weather.use_case

import com.vladislavgrom.weatherinfohelper.domain.util.Resource
import com.vladislavgrom.weatherinfohelper.domain.weather.model.WeatherInfo
import com.vladislavgrom.weatherinfohelper.domain.weather.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherDataUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
){
    suspend fun call(latitude: Double, longitude: Double) : Resource<WeatherInfo> {
        return weatherRepository.getWeatherData(latitude, longitude)
    }
}