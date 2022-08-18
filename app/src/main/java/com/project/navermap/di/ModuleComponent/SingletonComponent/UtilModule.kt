package com.project.navermap.di.ModuleComponent.SingletonComponent

import android.content.Context
import com.project.navermap.data.repository.restaurant.RestaurantRepository
import com.project.navermap.di.annotation.dispatchermodule.IoDispatcher
import com.project.navermap.domain.usecase.restaurantListViewModel.GetRestaurantListUseCaseImpl
import com.project.navermap.util.provider.ResourcesProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object UtilModule {

    @Provides
    fun provideResourcesProvider(@ApplicationContext context: Context
    ): ResourcesProviderImpl {
        return ResourcesProviderImpl(context)
    }

    @Provides
    fun provideRestaurantListUseCase(
        restaurantRepositoryImpl : RestaurantRepository,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): GetRestaurantListUseCaseImpl {
        return GetRestaurantListUseCaseImpl(restaurantRepositoryImpl, ioDispatcher)
    }
}