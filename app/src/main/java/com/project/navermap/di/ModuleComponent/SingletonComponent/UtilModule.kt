package com.project.navermap.di.ModuleComponent.SingletonComponent

import android.content.Context
import com.project.navermap.util.provider.ResourcesProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UtilModule {

    @Provides
    fun providedefaultResourcesProvider(@ApplicationContext context: Context
    ): ResourcesProviderImpl {
        return ResourcesProviderImpl(context)
    }
}