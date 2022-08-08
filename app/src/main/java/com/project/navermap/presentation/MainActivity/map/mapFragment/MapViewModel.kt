package com.project.navermap.presentation.MainActivity.map.mapFragment

import android.content.Context
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons
import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.data.entity.ShopInfoEntity
import com.project.navermap.domain.usecase.mapViewmodel.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel
@Inject
constructor(
    private val getShopListUseCaseImpl : GetShopListUseCaseImpl,
    private val getUpdateMarkerUseCaseImpl : GetUpdateMarkerUseCaseImpl,
    private val showMarkerUseCaseImlp : ShowMarkerUseCaseImlp,
    private val markerListenerUseCaseImpl : MarkerListenerUseCaseImpl,
    private val updateLocationUseCaseImpl : UpdateLocationUseCaseImpl
) : ViewModel() {

    private var naverMap: NaverMap? = null
    private var markers = mutableListOf<Marker>()
    lateinit var destLocation: LocationEntity
    var filterCategoryChecked = mutableListOf<Boolean>()

    private val _data = MutableLiveData<MapState>(MapState.Uninitialized)
    val data: LiveData<MapState> = _data

    fun setDestinationLocation(loc: LocationEntity) { destLocation = loc }
    fun setMap(m: NaverMap) { naverMap = m }
    fun getMap(): NaverMap? { return naverMap }

    //상점을 외부DB로 부터 가져온다
    fun loadShopList() = viewModelScope.launch {
        when (val result = getShopListUseCaseImpl.getApiShopList()) {
            is ShopResult.Success -> {
                val it = getShopListUseCaseImpl.getshopList()
                _data.value = MapState.Success(it)
            }
        }
    }

    //외부DB로 가져온 상점에 카테고리별로 다른 Marker를 적용한다.
    //실제 프로덕트에서는 실시간 데이터의 갱신이 있을 수 있어서 외부DB에서
    fun updateMarker() = viewModelScope.launch {
        val shopList = getShopEntityList()
        val naverMap = getMap()
        deleteMarkers()
        when (val result = getUpdateMarkerUseCaseImpl.updateMarker(filterCategoryChecked, shopList)) {
            is MarkerResult.Success -> {
                val it = getUpdateMarkerUseCaseImpl.getMarkers()
                markers = it as MutableList<Marker>
            }
        }
        deleteMarkers()
        showMarkerUseCaseImlp.showMarkersOnMap(naverMap, shopList, markers)
        markerListenerUseCaseImpl.setMarkerListener(markers)
    }

    fun updateLocation(location: LocationEntity) {
        deleteMarkers()
        val naverMap = getMap()
        // 위치 업데이트 될 때마다 목적지 마커 초기화
        updateLocationUseCaseImpl.updateLocation(location, naverMap)
    }

    fun getShopEntityList(): List<ShopInfoEntity>? {
        when (data.value) {
            is MapState.Success -> {
                return (data.value as MapState.Success).shopInfoList
            }
        }
        return null
    }

    fun deleteMarkers() {
        if (markers.isNullOrEmpty())
            return
        for (marker in markers) {
            marker.map = null
        }
    }

    //거리 계산은 서버에서
    fun calDist(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Long {

        val EARTH_R = 6371000.0
        val rad = Math.PI / 180
        val radLat1 = rad * lat1
        val radLat2 = rad * lat2
        val radDist = rad * (lon1 - lon2)

        var distance = Math.sin(radLat1) * Math.sin(radLat2)
        distance = distance + Math.cos(radLat1) * Math.cos(radLat2) * Math.cos(radDist)
        val ret = EARTH_R * Math.acos(distance)

        return Math.round(ret)
    }
}