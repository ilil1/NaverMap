package com.project.navermap.di.moduleComponent.activityComponent

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
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

    //Activity가 생성 됬을 때 inject가 이루어져서 NavHost가 아직 null이다.
    //lazy하게 이루어질 수 있도록 해야함.
    @Provides
    @ActivityScoped
    fun provideNavigationController(fragmentManager: FragmentManager) : NavController =
        (fragmentManager.findFragmentById(R.id.fragmentContainer) as NavHost).navController
}