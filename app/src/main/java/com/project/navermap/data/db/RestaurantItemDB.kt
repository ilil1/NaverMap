package com.project.navermap.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.navermap.data.db.dao.RestaurantItemCacheDao
import com.project.navermap.data.entity.restaurant.RestaurantFoodEntity


@Database(entities = [RestaurantFoodEntity::class], version = 1)
abstract class RestaurantItemDB : RoomDatabase() {

    abstract fun RestaurantItemCacheDao() : RestaurantItemCacheDao

}