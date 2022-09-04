package com.project.navermap.di.moduleComponent.singletonComponent

import android.content.Context
import androidx.room.Room
import com.project.navermap.data.db.MapDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMapDB(@ApplicationContext context: Context): MapDB =
        Room.databaseBuilder(context, MapDB::class.java, "MapStudy.db")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
}