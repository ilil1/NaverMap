package com.project.navermap.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.project.navermap.data.entity.AddressHistoryEntity

@Dao
interface AddressHistoryDao {
    @Query("SELECT * FROM MapStudy")
    suspend fun getAllAddresses(): List<AddressHistoryEntity>

    @Insert
    suspend fun insertAddress(address: AddressHistoryEntity)

    @Delete
    suspend fun deleteAddress(address: AddressHistoryEntity)

    @Query("delete from MapStudy")
    suspend fun deleteAllAddresses()
}