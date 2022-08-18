package com.project.navermap.di.ModuleComponent.SingletonComponent

import com.example.YUmarket.data.repository.suggest.SuggestRepositoryImpl
import com.project.navermap.data.repository.chat.ChatRepository
import com.project.navermap.data.repository.chat.DefaultChatRepository
import com.project.navermap.data.repository.home.HomeRepositoryImpl
import com.project.navermap.data.repository.home.HomeRepository
import com.project.navermap.data.repository.restaurant.RestaurantRepository
import com.project.navermap.data.repository.restaurant.RestaurantRepositoryImpl
import com.project.navermap.data.repository.suggest.SuggestRepository
import com.project.navermap.util.provider.ResourcesProviderImpl
import com.project.navermap.util.provider.ResourcesProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryBindModule {

    @Binds
    abstract fun bindResourcesProvider(
        resourcesProviderImpl: ResourcesProviderImpl
    ): ResourcesProvider

    @Binds
    abstract fun bindFoodApiRepository(
        restaurantRepositoryImpl: RestaurantRepositoryImpl
    ): RestaurantRepository

    @Binds
    abstract fun bindSuggestRepository(
        suggestRepositoryImpl: SuggestRepositoryImpl
    ): SuggestRepository

    @Binds
    abstract fun bindHomeRepository(
        homeRepositoryImpl: HomeRepositoryImpl
    ): HomeRepository

    @Binds
    abstract fun bindChatRepository(
        defaultChatRepository: DefaultChatRepository
    ): ChatRepository
}