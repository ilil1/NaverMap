package com.project.navermap.di.moduleComponent.singletonComponent

import com.project.navermap.data.db.MapDB
import com.project.navermap.data.network.MapApiService
import com.project.navermap.data.datasource.mapData.MapDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideMapDataSource(
        api: MapApiService,
        db: MapDB
    ) = MapDataSource(
        api = api,
        dao = db.mapSearchCacheDao()
    )
}