package com.project.navermap.data.db

import android.media.Image
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.project.navermap.data.db.dao.ImageDao
import com.project.navermap.domain.model.ProfileData
import com.project.navermap.util.Converters

@Database(entities = [ProfileData::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ImageDB : RoomDatabase() {

    abstract fun imageDao() : ImageDao

}