package com.project.navermap.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.project.navermap.data.entity.LikeMarketEntity

@Dao
interface LikeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(marketEntity: LikeMarketEntity)

}