package com.vladislavgrom.weatherinfohelper.common.di

import com.vladislavgrom.weatherinfohelper.data.weather.repository.WeatherRepositoryImpl
import com.vladislavgrom.weatherinfohelper.domain.weather.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WeatherRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        weatherRepositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository
}