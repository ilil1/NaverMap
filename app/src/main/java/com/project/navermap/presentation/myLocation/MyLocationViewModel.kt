package com.project.navermap.presentation.myLocation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.navermap.data.entity.AddressHistoryEntity
import com.project.navermap.data.entity.MapSearchInfoEntity
import com.project.navermap.data.repository.AddressHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyLocationViewModel
@Inject
constructor(
    private val addressHistoryRepository : AddressHistoryRepository
) : ViewModel() {

    private val _AddressesData = MutableLiveData<MyLocationState>(MyLocationState.Uninitialized)
    val AddressesData: LiveData<MyLocationState> = _AddressesData

    fun getAllAddresses() = viewModelScope.launch {
        _AddressesData.value = MyLocationState.Success(
            addressHistoryList = addressHistoryRepository.getAllAddresses()
        )
    }

    fun insertAddress(address: AddressHistoryEntity) = viewModelScope.launch {
        addressHistoryRepository.insertAddress(address)
    }

    fun deleteAddress(address: AddressHistoryEntity) = viewModelScope.launch {
        addressHistoryRepository.deleteAddress(address)
    }

    fun deleteAllAddresses() = viewModelScope.launch {
        addressHistoryRepository.deleteAllAddresses()
    }

    fun saveRecentSearchItems(entity: MapSearchInfoEntity) = viewModelScope.launch {
        val data = AddressHistoryEntity(
            id = null,
            fullAddress = entity.fullAddress,
            name = entity.name,
            lat = entity.locationLatLng.latitude,
            lng = entity.locationLatLng.longitude
        )
        insertAddress(data)
    }
}