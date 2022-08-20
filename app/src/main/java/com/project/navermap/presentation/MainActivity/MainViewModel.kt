package com.project.navermap.presentation.MainActivity

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.lifecycle.*
import com.naver.maps.geometry.LatLng
import com.project.navermap.R
import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.data.entity.MapSearchInfoEntity
import com.project.navermap.data.repository.map.MapApiRepository
import com.project.navermap.data.response.TmapAddress.AddressInfo
import com.project.navermap.domain.usecase.GetReverseGeoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val getReverseGeoUseCase: GetReverseGeoUseCase
) : ViewModel() {

    private val _locationData = MutableLiveData<MainState>(MainState.Uninitialized)
    val locationData: LiveData<MainState> = _locationData

    val destLocation
        get() = if (locationData.value is MainState.Success) {
            LatLng(
                (locationData.value as MainState.Success).mapSearchInfoEntity.locationLatLng.latitude,
                (locationData.value as MainState.Success).mapSearchInfoEntity.locationLatLng.longitude
            )
        } else {
            null
        }

    // TODO: 현재 위치 정보
    var curLocation: LatLng? = null
        private set

    private lateinit var locationManager: LocationManager


    @Suppress("CAST_NEVER_SUCCEEDS")
    fun getMapSearchInfo(): MapSearchInfoEntity? {
        when (locationData.value) {
            is MainState.Success -> {
                return (locationData.value as MainState.Success).mapSearchInfoEntity
            }
        }
        return null
    }

    fun getMyLocation(context: Context): Boolean {
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

        if (curLocation == null) {
            curLocation = LatLng(
                locationLatLngEntity.latitude,
                locationLatLngEntity.longitude
            )
        }

        val addressInfo = getReverseGeoUseCase(locationLatLngEntity)

        addressInfo?.let { addressInfoResult ->
            _locationData.value = MainState.Success(
                mapSearchInfoEntity = MapSearchInfoEntity(
                    fullAddress = addressInfoResult.fullAddress ?: "주소 정보 없음",
                    name = addressInfoResult.buildingName ?: "주소 정보 없음",
                    locationLatLng = locationLatLngEntity
                ),
                isLocationSame = false
            )
            Log.d("_locationData", _locationData.value.toString())
        } ?: MainState.Error(
            R.string.cannot_load_address_info
        )
    }
}
