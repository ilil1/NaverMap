package com.project.navermap.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.project.navermap.data.entity.MapSearchCacheEntity

@Dao
interface MapSearchCacheDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertSearchResults(entities: List<MapSearchCacheEntity>)

    @Query("SELECT * FROM map_search_cache")
    suspend fun getSearchResultCaches(): List<MapSearchCacheEntity>

    @Query("DELETE FROM map_search_cache")
    suspend fun clearSearchResultCaches()
}