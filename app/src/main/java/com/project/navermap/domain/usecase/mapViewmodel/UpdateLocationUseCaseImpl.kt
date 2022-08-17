package com.project.navermap.domain.usecase.mapViewmodel

import android.graphics.Color
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons
import com.project.navermap.data.entity.LocationEntity
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class UpdateLocationUseCaseImpl {
    private lateinit var destLocation: LocationEntity
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

    fun updateLocation(location: LocationEntity, naverMap: NaverMap?) {
        // 위치 업데이트 될 때마다 목적지 마커 초기화
        destLocation = location
        naverMap?.cameraPosition = CameraPosition(
            LatLng(
                destLocation.latitude,
                destLocation.longitude
            ), 15.0
        )
        destMarker.map = naverMap
    }
}