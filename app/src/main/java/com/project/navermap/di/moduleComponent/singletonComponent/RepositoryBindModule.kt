package com.project.navermap.di.moduleComponent.singletonComponent

import com.project.navermap.data.repository.suggest.SuggestRepositoryImpl
import com.project.navermap.data.repository.chat.ChatRepository
import com.project.navermap.data.repository.chat.ChatRepositoryImpl
import com.project.navermap.data.repository.firebaserealtime.*
import com.project.navermap.data.repository.home.*
import com.project.navermap.data.repository.map.MapApiRepository
import com.project.navermap.data.repository.map.MapApiRepositoryImpl
import com.project.navermap.data.repository.myinfo.CSRepository
import com.project.navermap.data.repository.myinfo.DefaultCSRepository
import com.project.navermap.data.repository.myinfo.ProfileRepository
import com.project.navermap.data.repository.myinfo.ProfileRepositoryImpl
import com.project.navermap.data.repository.restaurant.RestaurantRepository
import com.project.navermap.data.repository.restaurant.RestaurantRepositoryImpl
import com.project.navermap.data.repository.shop.ShopApiRepository
import com.project.navermap.data.repository.shop.ShopApiRepositoryImpl
import com.project.navermap.data.repository.suggest.SuggestRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryBindModule {

    @Binds
    abstract fun bindFoodApiRepository(
        restaurantRepositoryImpl: RestaurantRepositoryImpl
    ): RestaurantRepository

    @Binds
    abstract fun profileRepository(
      profileRepositoryImpl: ProfileRepositoryImpl
    ) : ProfileRepository

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
        chatRepositoryImpl: ChatRepositoryImpl
    ): ChatRepository

    @Binds
    abstract fun bindMapApiRepository(
        mapApiRepositoryImpl: MapApiRepositoryImpl
    ): MapApiRepository

    @Binds
    abstract fun bindShopApiRepository(
        shopApiRepositoryImpl: ShopApiRepositoryImpl
    ): ShopApiRepository

    @Binds
    abstract fun bindCsRepository(
        defaultCSRepository: DefaultCSRepository
    ) : CSRepository

    @Binds
    abstract fun bindHomeFirstRepository(
        homeFirstMockRepositoryImpl: HomeFirstMockRepositoryImpl
    ) : HomeFirstMockRepository

    @Binds
    abstract fun bindHomeSecondRepository(
        homeSecondMockRepositoryImpl: HomeSecondMockRepositoryImpl
    ) : HomeSecondMockRepository

    @Binds
    abstract fun bindFirebaseRepository(
        firebaseRepositoryImpl: firebaseRepositoryImpl
    ): firebaseRepository

    @Binds
    abstract fun bindReviewRepository(
        reviewRepositoryImpl: ReviewRepositoryImpl
    ) : ReviewRepository

    @Binds
    abstract fun bindItemRepository(
        repository: FirebaseItemRepository
    ): ItemRepository
}