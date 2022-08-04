package com.project.navermap.di

import android.content.Context
import androidx.room.Room
import com.project.navermap.data.db.MapDB
import com.project.navermap.data.db.dao.AddressHistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMapDB(@ApplicationContext context: Context): MapDB =
        Room.databaseBuilder(context, MapDB::class.java, "MapStudy.db")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
}