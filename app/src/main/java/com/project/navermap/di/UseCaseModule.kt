package com.project.navermap.di

import com.project.navermap.data.repository.ShopApiRepository
import com.project.navermap.di.annotation.dispatchermodule.IoDispatcher
import com.project.navermap.domain.usecase.mapViewmodel.GetShopListUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ApplicationComponent::class)
object UseCaseModule {

    @Provides
    fun provideShopListUseCase(
        shopApiRepositoryImpl : ShopApiRepository,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): GetShopListUseCaseImpl {
        return GetShopListUseCaseImpl(shopApiRepositoryImpl, ioDispatcher)
    }
}