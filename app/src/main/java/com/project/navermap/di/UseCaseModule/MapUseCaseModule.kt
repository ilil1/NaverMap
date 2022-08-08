package com.project.navermap.di.UseCaseModule

import android.content.Context
import com.project.navermap.data.repository.ShopApiRepository
import com.project.navermap.di.annotation.dispatchermodule.IoDispatcher
import com.project.navermap.domain.usecase.mapViewmodel.*
import com.project.navermap.presentation.MainActivity.store.restaurant.RestautantFilterOrder
import com.project.navermap.util.provider.DefaultResourcesProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
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
    fun provideMarkerListenerUseCase(@ApplicationContext context: Context
    ): MarkerListenerUseCaseImpl {
        return MarkerListenerUseCaseImpl(context)
    }

    @Provides
    fun provideUpdateLocationUseCase(
    ): UpdateLocationUseCaseImpl {
        return UpdateLocationUseCaseImpl()
    }

    @Provides
    fun providedefaultResourcesProvider(@ApplicationContext context: Context
    ): DefaultResourcesProvider {
        return DefaultResourcesProvider(context)
    }
}