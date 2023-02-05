package com.project.navermap.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.project.navermap.data.db.dao.LikeDao
import com.project.navermap.data.entity.LikeMarketEntity

@Database(entities = [LikeMarketEntity::class], version = 1)
abstract class LikeDB : RoomDatabase() {

    companion object {
        private var instance : LikeDB? = null
        fun getInstance(_context : Context) : LikeDB? {
            if (instance == null){
                synchronized(LikeDB::class){
                    instance = Room.databaseBuilder(
                        _context.applicationContext,
                        LikeDB::class.java, "Like.db"
                    )
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return instance
        }
    }

    abstract fun likeDataDao() : LikeDao
}