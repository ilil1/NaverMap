package com.project.navermap.di.moduleComponent.singletonComponent

import com.project.navermap.data.datasource.restaurant.RestaurantDataSource
import com.project.navermap.data.datasource.restaurant.RestaurantDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceBindModule {

    @Binds
    abstract fun bindRestaurantDataSource(
        restaurantDataSourceImpl: RestaurantDataSourceImpl
    ): RestaurantDataSource
}