package com.project.navermap.presentation.MainActivity.map.mapFragment.navermap

import android.content.ContentValues.TAG
import android.util.Log
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.project.navermap.domain.model.RestaurantModel

typealias MarkerClickListener = Marker.(Overlay) -> Boolean

class NaverMapHandler(
    private val markerFactory: MarkerFactory,
    private val naverMap: NaverMap
) {

    /**
     * Handler의 지도에 띄울 마커들
     */

    private var markers = emptyList<Marker>()
    //private var markers = mutableListOf<Marker>()
    /**
     * 목적지 마커를 띄우는 함수
     */
    fun updateDestMarker(destMarker: Marker, location: LatLng) {
        destMarker.position = location
        destMarker.map = naverMap
    }

    /**
     * 카메라를 이동하는 함수
     * @param location 이동할 위치
     * @param onError 실패시 수행할 동작
     */
    fun moveCameraTo(location: LatLng, onError: () -> Unit) {
        try {
            naverMap.cameraPosition = CameraPosition(location, 15.0)
        } catch (ex: Exception) {
            onError()
        }
    }

    /**
     * Handler의 지도에서 마커들을 삭제
     */
    fun deleteMarkers() {
        for (marker in markers) {
            //Log.d("markers", markers.toString())
            marker.map = null
        }
    }

    /**
     * Handler의 지도에 마커들을 표시
     */
    fun showMarkers() {
        for (marker in markers) {
            marker.map = naverMap
        }
    }

    /**
     * 주변 가게들에 대해 마커를 띄우는 함수
     * @param restaurantInfoList 마커를 띄울 가게 리스트
     */
    fun updateRestaurantMarkers(
        restaurantInfoList: List<RestaurantModel>,
        clickListener: MarkerClickListener
    ) {
        deleteMarkers()
        markers = restaurantInfoList.mapIndexed { index, restaurant ->
            markerFactory.createMarker(
                position = LatLng(restaurant.latitude, restaurant.longitude),
                category = restaurant.restaurantCategory,
                tag = restaurant,
                zIndex = index,
            ).apply {
                setOnClickListener { overlay -> clickListener(this, overlay) }
            }
        }.also { showMarkers() }
    }
}