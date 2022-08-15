package com.project.navermap.presentation.myLocation.mapLocationSetting

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.navermap.R
import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.data.entity.MapSearchInfoEntity
import com.project.navermap.data.repository.map.MapApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapLocationSettingViewModel
@Inject
constructor(
    private val mapApiRepositoryImpl: MapApiRepository
) : ViewModel() {

    private val _locationData = MutableLiveData<MapLocationSettingState>(MapLocationSettingState.Uninitialized)
    val locationData: LiveData<MapLocationSettingState> = _locationData

    fun getMapSearchInfo(): MapSearchInfoEntity? {
        when (val data = locationData.value) {
            is MapLocationSettingState.Success -> {
                return data.mapSearchInfoEntity
            }
        }
        return null
    }

    fun getReverseGeoInformation(
        locationLatLngEntity: LocationEntity
    ) = viewModelScope.launch {

        val currentLocation = locationLatLngEntity
        val addressInfo = mapApiRepositoryImpl.getReverseGeoInformation(locationLatLngEntity)

        addressInfo?.let { addressInfoResult ->
            _locationData.value = MapLocationSettingState.Success(
                mapSearchInfoEntity = MapSearchInfoEntity(
                    fullAddress = addressInfoResult.fullAddress ?: "주소 정보 없음",
                    name = addressInfoResult.buildingName ?: "주소 정보 없음",
                    locationLatLng = currentLocation
                ),
                isLocationSame = false
            )
            Log.d("_locationData", _locationData.value.toString())
        } ?: MapLocationSettingState.Error(
            R.string.cannot_load_address_info
        )
    }

}