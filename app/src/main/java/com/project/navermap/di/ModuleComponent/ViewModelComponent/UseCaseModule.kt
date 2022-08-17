package com.project.navermap.di.ModuleComponent.ViewModelComponent

import com.project.navermap.data.repository.restaurant.RestaurantRepository
import com.project.navermap.data.repository.shop.ShopApiRepository
import com.project.navermap.di.annotation.dispatchermodule.IoDispatcher
import com.project.navermap.domain.usecase.mapViewmodel.GetItemsByRestaurantIdUseCase
import com.project.navermap.domain.usecase.mapViewmodel.LegacyShop.GetShopListUseCaseImpl
import com.project.navermap.domain.usecase.mapViewmodel.UpdateLocationUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    fun provideShopListUseCase(
        shopApiRepositoryImpl: ShopApiRepository,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ) = GetShopListUseCaseImpl(shopApiRepositoryImpl, ioDispatcher)

    @Provides
    fun provideUpdateLocationUseCase() = UpdateLocationUseCaseImpl()

    @Provides
    fun provideGetItemsByRestaurantIdUseCase(repository: RestaurantRepository) =
        GetItemsByRestaurantIdUseCase(repository)
}