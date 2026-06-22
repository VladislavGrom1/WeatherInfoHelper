package com.vladislavgrom.weatherinfohelper.data.weather.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import com.vladislavgrom.weatherinfohelper.data.weather.model.WeatherDataDto
import com.vladislavgrom.weatherinfohelper.data.weather.model.WeatherDto
import com.vladislavgrom.weatherinfohelper.domain.weather.model.WeatherData
import com.vladislavgrom.weatherinfohelper.domain.weather.model.WeatherInfo
import com.vladislavgrom.weatherinfohelper.domain.weather.model.WeatherType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private data class IndexedWeatherData(
    val index: Int,
    val data: WeatherData
)

fun WeatherDataDto.toWeatherDataMap(): Map<Int, List<WeatherData>> {
    return time.mapIndexed { index, time ->
        val temperature = temperature_2m[index]
        val weatherCode = weathercode[index]
        val windSpeed = windspeed_10m[index]
        val pressure = pressure_msl[index]
        val humidity = relativehumidity_2m[index]

        IndexedWeatherData(
            index = index,
            data = WeatherData(
                time = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME),
                temperatureC = temperature,
                pressure = pressure,
                windSpeed = windSpeed,
                humidity = humidity.toDouble(),
                weatherType = WeatherType.fromWMO(weatherCode)
            )
        )
    }.groupBy {
        it.index / 24
    }.mapValues {
        it.value.map { it.data }
    }
}

fun WeatherDto.toWeatherInfo(): WeatherInfo {
    val weatherDataMap = hourly.toWeatherDataMap()
    val now = LocalDateTime.now()
    val currentWeatherData = weatherDataMap[0]?.find {
        val hour = if (now.minute < 30) now.hour else now.hour + 1
        it.time.hour == hour
    }
    return WeatherInfo(
        weatherDataPerDay = weatherDataMap,
        currentWeatherData = currentWeatherData
    )
}