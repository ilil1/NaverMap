package com.project.navermap.presentation.MainActivity.map.mapFragment.navermap

import android.graphics.Color
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.project.navermap.R
import com.project.navermap.presentation.MainActivity.store.restaurant.RestaurantCategory
import javax.inject.Inject

class MarkerFactory @Inject constructor() {

    fun createMarker(
        position: LatLng,
        category: RestaurantCategory,
        tag: Any,
        zIndex: Int = 1,
    ) = Marker().apply {
        this.position = position
        this.tag = tag
        this.zIndex = zIndex
        setMarkerIconAndColor(this, category)
    }

    private fun setMarkerIconAndColor(marker: Marker, category: RestaurantCategory) = with(marker) {
        when (category.ordinal) {
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