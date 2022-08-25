package com.project.navermap.di.ModuleComponent.FragmentComponent

import androidx.fragment.app.Fragment
import com.naver.maps.map.NaverMap
import com.project.navermap.presentation.mainActivity.map.mapFragment.MapFragment
import com.project.navermap.presentation.mainActivity.map.mapFragment.navermap.MarkerFactory
import com.project.navermap.presentation.mainActivity.map.mapFragment.navermap.NaverMapHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module
@InstallIn(FragmentComponent::class)
object MapModule {
    @Provides
    fun provideMapFragment(fragment: Fragment) = fragment as MapFragment

    @Provides
    fun provideNaverMap(mapFragment: MapFragment) = mapFragment.naverMap

    @Provides
    fun provideMarkerFactory() = MarkerFactory()

    @Provides
    @FragmentScoped
    fun provideNaverMapHandler(
        markerFactory: MarkerFactory,
        naverMap: NaverMap
    ) = NaverMapHandler(markerFactory, naverMap)

}