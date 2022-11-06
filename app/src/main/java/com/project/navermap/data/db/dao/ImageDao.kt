package com.project.navermap.data.db.dao

import androidx.room.*
import com.project.navermap.data.entity.AddressHistoryEntity
import com.project.navermap.domain.model.ImageData
import com.project.navermap.domain.model.ProfileData
import dagger.Provides

@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(profileData: ProfileData)

    @Update
    suspend fun updateData(profileData: ProfileData)

    @Query("SELECT * FROM profile")
    suspend fun getAllAddresses() : ProfileData


    @Query("SELECT (SELECT COUNT(*)FROM profile) == 0")
    fun isEmpty() : Boolean

}