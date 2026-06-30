package com.vladislavgrom.weatherinfohelper.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.jordond.compass.geocoder.Geocoder
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GeocoderModule {

    @Provides
    @Singleton
    fun provideGeocoder(): Geocoder{
        return Geocoder()
    }
}