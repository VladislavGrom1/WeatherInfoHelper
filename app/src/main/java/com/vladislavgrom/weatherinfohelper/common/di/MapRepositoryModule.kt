package com.vladislavgrom.weatherinfohelper.common.di

import com.vladislavgrom.weatherinfohelper.data.map.repository.MapRepositoryImpl
import com.vladislavgrom.weatherinfohelper.domain.map.repository.MapRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MapRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMapRepository(impl: MapRepositoryImpl): MapRepository
}