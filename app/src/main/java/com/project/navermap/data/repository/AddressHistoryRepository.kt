package com.project.navermap.data.repository

import androidx.lifecycle.viewModelScope
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.project.navermap.data.db.MapDB
import com.project.navermap.data.db.dao.AddressHistoryDao
import com.project.navermap.data.entity.AddressHistoryEntity
import com.project.navermap.di.annotation.dispatchermodule.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddressHistoryRepository @Inject constructor(
    private val addressHistoryDB : MapDB,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getAllAddresses() = withContext(ioDispatcher) {
        addressHistoryDB.addressHistoryDao().getAllAddresses()
    }

    suspend fun insertAddress(address: AddressHistoryEntity) = withContext(ioDispatcher) {
        addressHistoryDB.addressHistoryDao().insertAddress(address)
    }

    suspend fun deleteAddress(address: AddressHistoryEntity) = withContext(ioDispatcher) {
        addressHistoryDB.addressHistoryDao().deleteAddress(address)
    }
    suspend fun deleteAllAddresses() = withContext(ioDispatcher) {
        addressHistoryDB.addressHistoryDao().deleteAllAddresses()
    }
}