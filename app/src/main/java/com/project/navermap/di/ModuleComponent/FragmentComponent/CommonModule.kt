package com.project.navermap.di.ModuleComponent.FragmentComponent

import android.content.Context
import com.project.navermap.data.repository.ShopApiRepository
import com.project.navermap.di.annotation.dispatchermodule.IoDispatcher
import com.project.navermap.domain.usecase.mapViewmodel.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(FragmentComponent::class)
object CommonModule {
}