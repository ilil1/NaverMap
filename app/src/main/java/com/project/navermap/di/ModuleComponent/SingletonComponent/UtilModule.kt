package com.project.navermap.di.ModuleComponent.SingletonComponent

import com.project.navermap.util.provider.ResourcesProvider
import com.project.navermap.util.provider.ResourcesProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UtilModule {
    @Binds
    abstract fun bindResourcesProvider(
        resourcesProvider: ResourcesProviderImpl
    ): ResourcesProvider
}