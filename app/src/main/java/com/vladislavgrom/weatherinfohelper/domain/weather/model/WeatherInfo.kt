package com.vladislavgrom.weatherinfohelper.domain.weather.model

data class WeatherInfo (
    val weatherDataPerDay: Map<Int, List<WeatherData>>,
    val currentWeatherData: WeatherData?
)