package com.project.navermap.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.navermap.data.entity.LikeMarketEntity

@Dao
interface LikeDao {

    @Query("SELECT * FROM `Like`")
    suspend fun getAllLikeData() : List<LikeMarketEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(marketEntity: LikeMarketEntity)

    @Query("delete from `Like`")
    suspend fun deleteAllLike()

}