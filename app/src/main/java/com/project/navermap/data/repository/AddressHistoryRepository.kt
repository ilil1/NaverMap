package com.project.navermap.data.repository

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.project.navermap.data.db.dao.AddressHistoryDao
import com.project.navermap.data.entity.AddressHistoryEntity
import javax.inject.Inject

class AddressHistoryRepository @Inject constructor(
    private val addressHistoryDao: AddressHistoryDao
) {
    suspend fun getAllAddresses(): List<AddressHistoryEntity>
    = addressHistoryDao.getAllAddresses()

    suspend fun insertAddress(address: AddressHistoryEntity)
    = addressHistoryDao.insertAddress(address)

    suspend fun deleteAddress(address: AddressHistoryEntity)
    = addressHistoryDao.deleteAddress(address)

    suspend fun deleteAllAddresses()
    = addressHistoryDao.deleteAllAddresses()
}