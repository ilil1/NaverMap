package com.project.navermap.di.moduleComponent.activityComponent

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavHost
import com.project.navermap.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object NavigationModule {

    @Provides
    fun provideActivity(activity: Activity) = activity as AppCompatActivity

    @Provides
    fun provideFragmentManager(activity: AppCompatActivity) = activity.supportFragmentManager

    @Provides
    @ActivityScoped
    fun provideNavigationController(fragmentManager: FragmentManager) =
        (fragmentManager.findFragmentById(R.id.fragmentContainer) as NavHost).navController
}