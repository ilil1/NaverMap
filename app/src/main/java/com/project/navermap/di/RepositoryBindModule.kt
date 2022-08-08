package com.project.navermap.di

import com.project.navermap.data.repository.MapApiRepository
import com.project.navermap.data.repository.MapApiRepositoryImpl
import com.project.navermap.data.repository.ShopApiRepository
import com.project.navermap.data.repository.ShopApiRepositoryImpl
import com.project.navermap.data.repository.restaurant.DefaultRestaurantRepository
import com.project.navermap.data.repository.restaurant.RestaurantRepository
import com.project.navermap.presentation.MainActivity.store.restaurant.RestautantFilterOrder
import com.project.navermap.util.provider.DefaultResourcesProvider
import com.project.navermap.util.provider.ResourcesProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryBindModule {

    @Binds
    abstract fun bindMapApiRepository(
        mapApiRepositoryImpl: MapApiRepositoryImpl
    ): MapApiRepository

    @Binds
    abstract fun bindShopApiRepository(
        shopApiRepositoryImpl: ShopApiRepositoryImpl
    ): ShopApiRepository

    @Binds
    abstract fun bindFoodApiRepository(
        defaultRestaurantRepository: DefaultRestaurantRepository
    ): RestaurantRepository

    @Binds
    abstract fun bindResourcesProvider(
        defaultResourcesProvider: DefaultResourcesProvider
    ): ResourcesProvider
}