package com.project.navermap.screen.MainActivity

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.project.navermap.R
import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.data.entity.MapSearchInfoEntity
import com.project.navermap.data.repository.MapApiRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MainViewModel
@ViewModelInject
constructor(
    private val mapApiRepositoryImpl: MapApiRepository
) : ViewModel() {

    private lateinit var destLocation: LocationEntity
    lateinit var curLocation: Location

    private val _locationData = MutableLiveData<MainState>(MainState.Uninitialized)
    val locationData: LiveData<MainState> = _locationData

    private lateinit var locationManager: LocationManager

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

    @Suppress("CAST_NEVER_SUCCEEDS")
    fun getMapSearchInfo(): MapSearchInfoEntity? {
        when (locationData.value) {
            is MainState.Success -> {
                return (locationData.value as MainState.Success).mapSearchInfoEntity
            }
        }
        return null
    }

    fun getMyLocation(context : Context) : Boolean {
        if (::locationManager.isInitialized.not()) {
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
        val isGpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        return isGpsEnable
    }

    fun getLocationManager(): LocationManager {
        return locationManager
    }

    fun getReverseGeoInformation(
        locationLatLngEntity: LocationEntity
    ) = viewModelScope.launch {

        val currentLocation = locationLatLngEntity
        val addressInfo = mapApiRepositoryImpl.getReverseGeoInformation(locationLatLngEntity)

        addressInfo?.let { addressInfoResult ->
            _locationData.value = MainState.Success(
                mapSearchInfoEntity = MapSearchInfoEntity(
                    fullAddress = addressInfoResult.fullAddress ?: "주소 정보 없음",
                    name = addressInfoResult.buildingName ?: "주소 정보 없음",
                    locationLatLng = currentLocation
                ),
                isLocationSame = false
            )
            Log.d("_locationData", _locationData.value.toString())
        } ?: MainState.Error(
            R.string.cannot_load_address_info
        )
    }
}
