package com.vladislavgrom.weatherinfohelper.common.di

import com.vladislavgrom.weatherinfohelper.common.location.LocationRepositoryImpl
import com.vladislavgrom.weatherinfohelper.domain.location.repository.LocationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindLocationTracker(locationRepositoryImpl: LocationRepositoryImpl) : LocationRepository
}