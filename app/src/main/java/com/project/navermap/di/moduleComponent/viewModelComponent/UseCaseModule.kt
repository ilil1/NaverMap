package com.project.navermap.di.moduleComponent.viewModelComponent

import com.project.navermap.data.repository.map.MapApiRepository
import com.project.navermap.data.repository.restaurant.RestaurantRepository
import com.project.navermap.di.annotation.dispatchermodule.IoDispatcher
import com.project.navermap.domain.usecase.mainViewmodel.GetReverseGeoUseCase
import com.project.navermap.domain.usecase.mapViewmodel.GetItemsByRestaurantIdUseCase
import com.project.navermap.domain.usecase.restaurantListViewModel.GetRestaurantListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideGetShopListUseCase(
        repository: RestaurantRepository
    ) = GetRestaurantListUseCase(repository)

    @Provides
    fun provideGetItemsByRestaurantIdUseCase(repository: RestaurantRepository) =
        GetItemsByRestaurantIdUseCase(repository)

    @Provides
    fun provideGetReverseGeoUseCase(repository: MapApiRepository) =
        GetReverseGeoUseCase(repository)
//
//    @Provides
//    fun provideProfileRepository(repository: ProfileRepository) = GetRoomUseCase(repository)

}