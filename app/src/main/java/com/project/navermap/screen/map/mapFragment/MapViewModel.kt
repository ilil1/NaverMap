package com.project.navermap.screen.map.mapFragment

import android.graphics.Color
import androidx.lifecycle.ViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons
import com.project.navermap.data.entity.LocationEntity

class MapViewModel() : ViewModel() {

    lateinit var destLocation: LocationEntity
    private var naverMap: NaverMap? = null
    private var markers = mutableListOf<Marker>()

    private var destMarker: Marker = Marker(
        MarkerIcons.BLACK
    ).apply {
        zIndex = 111
        iconTintColor = Color.parseColor("#FA295B")
        width = 100
        height = 125
    }
        get() = field.apply {
            position = LatLng(
                destLocation.latitude,
                destLocation.longitude
            )
        }

    fun setDestinationLocation(loc: LocationEntity) { destLocation = loc }
    fun setMap(m: NaverMap) { naverMap = m }
    fun getMap(): NaverMap? { return naverMap }

    /**
     * 네이버 지도상 마커를 모두 없애는 method
     */
    fun deleteMarkers() {
        if (markers.isNullOrEmpty())
            return
        for (marker in markers) {
            marker.map = null
        }
    }

    fun firstupdateLocation() {
        naverMap?.cameraPosition = CameraPosition(
            LatLng(
                destLocation.latitude,
                destLocation.longitude
            ), 15.0
        )
        destMarker.map = naverMap
    }

    fun updateLocation(location: LocationEntity) {
        // 위치 업데이트 될 때마다 목적지 마커 초기화
        destLocation = location
        deleteMarkers()
        naverMap?.cameraPosition = CameraPosition(
            LatLng(
                destLocation.latitude,
                destLocation.longitude
            ), 15.0
        )
        destMarker.map = naverMap
    }
}