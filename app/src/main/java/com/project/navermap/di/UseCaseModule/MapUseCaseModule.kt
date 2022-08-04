package com.project.navermap.di.UseCaseModule

import com.project.navermap.data.repository.ShopApiRepository
import com.project.navermap.di.annotation.dispatchermodule.IoDispatcher
import com.project.navermap.domain.usecase.mapViewmodel.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ApplicationComponent::class)
object MapUseCaseModule {

    @Provides
    fun provideShopListUseCase(
        shopApiRepositoryImpl : ShopApiRepository,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): GetShopListUseCaseImpl {
        return GetShopListUseCaseImpl(shopApiRepositoryImpl, ioDispatcher)
    }

    @Provides
    fun provideUpdateMarkerUseCase(
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): GetUpdateMarkerUseCaseImpl {
        return GetUpdateMarkerUseCaseImpl(ioDispatcher)
    }

    @Provides
    fun provideShowMarkerUseCase(
        getUpdateMarkerUseCaseImpl : GetUpdateMarkerUseCaseImpl,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): ShowMarkerUseCaseImlp {
        return ShowMarkerUseCaseImlp(getUpdateMarkerUseCaseImpl, ioDispatcher)
    }

    @Provides
    fun provideMarkerListenerUseCase(
    ): MarkerListenerUseCaseImpl {
        return MarkerListenerUseCaseImpl()
    }

    @Provides
    fun provideUpdateLocationUseCase(
    ): UpdateLocationUseCaseImpl {
        return UpdateLocationUseCaseImpl()
    }
}