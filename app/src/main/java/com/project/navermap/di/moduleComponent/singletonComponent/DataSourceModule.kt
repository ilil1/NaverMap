package com.project.navermap.di.moduleComponent.singletonComponent

import com.project.navermap.data.db.MapDB
import com.project.navermap.data.network.MapApiService
import com.project.navermap.data.datasource.mapData.MapDataSource
import com.project.navermap.di.annotation.dispatchermodule.IoDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideMapDataSource(
        api: MapApiService,
        db: MapDB,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ) = MapDataSource(
        api = api,
        dao = db.mapSearchCacheDao(),
        ioDispatcher
    )
}