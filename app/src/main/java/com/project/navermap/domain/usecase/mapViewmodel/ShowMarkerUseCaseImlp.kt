package com.project.navermap.domain.usecase.mapViewmodel

import android.graphics.Color
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.project.navermap.R
import com.project.navermap.data.entity.ShopInfoEntity
import com.project.navermap.domain.model.RestaurantModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ShowMarkerUseCaseImlp @Inject constructor(
    private val getUpdateMarkerUseCaseImpl : GetUpdateMarkerUseCaseImpl
) {
    /**
     * 네이버 지도상에 마커를 표시
     */
    fun showMarkersOnMap(naverMap: NaverMap?,
                         restaurantList: List<RestaurantModel>?,
                         markers : List<Marker>) {

        if (markers.isNullOrEmpty())
            return

        for (marker in markers) {
            marker.map = naverMap
            setMarkerIconAndColor(marker,
                getCategoryNum(restaurantList?.get(marker.zIndex)!!.restaurantCategory.toString()))
        }
    }

    private fun getCategoryNum(category: String): Int =
        when (category) {
            "ALL" -> 0
            "KOREAN_FOOD" -> 1
            "DUMPLING_FOOD" -> 2
            "CAFE_DESSERT" -> 3
            "JAPANESE_FOOD" -> 4
            else -> 5
        }

    fun setMarkerIconAndColor(marker: Marker, category: Int) = with(marker) {
        when (category) {
            0 -> {
                icon = OverlayImage.fromResource(R.drawable.marker_m)
                iconTintColor = Color.parseColor("#46F5FF")
            }
            1 -> {
                icon = OverlayImage.fromResource(R.drawable.marker_r)
                iconTintColor = Color.parseColor("#FFCB41")
            }
            2 -> {
                icon = OverlayImage.fromResource(R.drawable.marker_s)
                iconTintColor = Color.parseColor("#886AFF")
            }
            3 -> {
                icon = OverlayImage.fromResource(R.drawable.marker_e)
                iconTintColor = Color.parseColor("#04B404")
            }
            4 -> {
                icon = OverlayImage.fromResource(R.drawable.marker_f)
                iconTintColor = Color.parseColor("#8A0886")
            }
            5 -> {
                icon = OverlayImage.fromResource(R.drawable.marker_f)
                iconTintColor = Color.parseColor("#0B2F3A")
            }
        }
    }
}