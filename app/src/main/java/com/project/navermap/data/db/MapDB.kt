package com.project.navermap.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.project.navermap.data.db.dao.AddressHistoryDao
import com.project.navermap.data.entity.AddressHistoryEntity

@Database(entities = [AddressHistoryEntity::class], version = 1)
abstract class MapDB : RoomDatabase() {

    //hilt로 인한 레거시
    companion object {
        private var instance: MapDB? = null
        fun getInstance(_context: Context): MapDB? {
            if(instance == null) {
                synchronized(MapDB::class) {
                    instance = Room.databaseBuilder(_context.applicationContext,
                        MapDB::class.java, "MapStudy.db")
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return instance
        }
    }

    abstract fun addressHistoryDao(): AddressHistoryDao
}
