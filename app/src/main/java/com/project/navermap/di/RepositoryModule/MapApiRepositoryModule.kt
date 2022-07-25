package com.project.navermap.di.RepositoryModule

import com.project.navermap.data.repository.MapApiRepository
import com.project.navermap.data.repository.MapApiRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class MapApiRepositoryModule {

    @Binds
    abstract fun bindMapApiRepository(
        mapApiRepositoryImpl: MapApiRepositoryImpl
    ): MapApiRepository

}