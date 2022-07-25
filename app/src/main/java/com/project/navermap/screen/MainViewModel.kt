package com.project.navermap.screen

import android.location.Location
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.data.entity.MapSearchInfoEntity
import com.project.navermap.data.repository.MapApiRepository
import com.project.navermap.data.repository.MapApiRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainViewModel
@ViewModelInject
constructor(
    private val mapApiImpl: MapApiRepository
) : ViewModel() {

    private lateinit var destLocation: LocationEntity
    lateinit var curLocation: Location
    lateinit var mapSearchInfoEntity: MapSearchInfoEntity

    fun setCurrentLocation(loc: Location) {
        curLocation = loc
    }

    fun getCurrentLocation(): Location {
        return curLocation
    }

    fun setDestinationLocation(loc: LocationEntity) {
        destLocation = loc
    }

    fun getDestinationLocation(): LocationEntity {
        return destLocation
    }

    fun getReverseGeoInformation(
        locationLatLngEntity: LocationEntity
    ) = viewModelScope.launch {

        val currentLocation = locationLatLngEntity
        val response = mapApiImpl.getReverseGeoInformation(locationLatLngEntity)

        response?.let { response ->
            mapSearchInfoEntity = MapSearchInfoEntity(
                fullAddress = response.fullAddress ?: "주소 정보 없음",
                name = response.buildingName ?: "주소 정보 없음",
                locationLatLng = currentLocation
            )

            Log.d("getReverseGeo", response.fullAddress.toString())

        } ?: null
    }
}
