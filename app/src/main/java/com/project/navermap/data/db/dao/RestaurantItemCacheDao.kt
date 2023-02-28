package com.project.navermap.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.navermap.data.entity.restaurant.RestaurantFoodEntity

@Dao
interface RestaurantItemCacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchResults(entities: List<RestaurantFoodEntity>)

    @Query("SELECT * FROM restaurant_food_cache")
    suspend fun getSearchResultCaches(): List<RestaurantFoodEntity>

    @Query("DELETE FROM restaurant_food_cache")
    suspend fun clearSearchResultCaches()
}