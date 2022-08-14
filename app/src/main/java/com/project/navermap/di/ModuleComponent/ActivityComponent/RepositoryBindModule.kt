package com.project.navermap.di.ModuleComponent.ActivityComponent

import com.project.navermap.data.repository.MapApiRepository
import com.project.navermap.data.repository.MapApiRepositoryImpl
import com.project.navermap.data.repository.ShopApiRepository
import com.project.navermap.data.repository.ShopApiRepositoryImpl
import com.project.navermap.data.repository.restaurant.RestaurantRepositoryImpl
import com.project.navermap.data.repository.restaurant.RestaurantRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class RepositoryBindModule {

    @Binds
    abstract fun bindMapApiRepository(
        mapApiRepositoryImpl: MapApiRepositoryImpl
    ): MapApiRepository

    @Binds
    abstract fun bindShopApiRepository(
        shopApiRepositoryImpl: ShopApiRepositoryImpl
    ): ShopApiRepository


}