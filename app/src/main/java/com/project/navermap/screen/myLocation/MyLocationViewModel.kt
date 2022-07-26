package com.project.navermap.screen.myLocation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.project.navermap.data.entity.AddressHistoryEntity
import com.project.navermap.data.repository.AddressHistoryRepository
import com.project.navermap.data.repository.ShopApiRepository

class MyLocationViewModel
@ViewModelInject
constructor(
    private val addressHistoryRepository : AddressHistoryRepository
) : ViewModel() {

    suspend fun getAllAddresses() : List<AddressHistoryEntity> {
        return addressHistoryRepository.getAllAddresses()
    }

    suspend fun insertAddress(address: AddressHistoryEntity)
            = addressHistoryRepository.insertAddress(address)

    suspend fun deleteAddress(address: AddressHistoryEntity)
            = addressHistoryRepository.deleteAddress(address)

    suspend fun deleteAllAddresses()
            = addressHistoryRepository.deleteAllAddresses()
}