package com.project.navermap.di.RepositoryModule

import com.project.navermap.data.network.MapApiService
import com.project.navermap.data.repository.MapApiRepository
import com.project.navermap.data.repository.MapApiRepositoryImpl
import com.project.navermap.di.annotation.IoDispatcher
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

//    @Singleton
//    @Provides
//    fun provideMapApiRepository(
//        mapApiService: MapApiService,
//        @IoDispatcher ioDispatcher: CoroutineDispatcher,
//    ): MapApiRepositoryImpl {
//        return MapApiRepositoryImpl(mapApiService, ioDispatcher)
//    }
}