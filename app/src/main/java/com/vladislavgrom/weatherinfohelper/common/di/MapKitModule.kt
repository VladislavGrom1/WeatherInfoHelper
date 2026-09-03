package com.vladislavgrom.weatherinfohelper.common.di
import com.vladislavgrom.weatherinfohelper.BuildConfig
import android.content.Context
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManager
import com.yandex.mapkit.search.SearchManagerType
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapKitModule {

    @Provides
    @Singleton
    fun provideMapKit(): MapKit = MapKitFactory.getInstance()

    @Provides
    @Singleton
    fun provideSearchManager(): SearchManager {
        return SearchFactory.getInstance().createSearchManager(SearchManagerType.ONLINE)
    }
}