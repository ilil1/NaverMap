package com.project.navermap.extensions

import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker



fun List<Marker>.showOnMap(map: NaverMap) {
    for (marker in this) {
        marker.map = map
    }
}

fun List<Marker>.deleteOnMap() {
    for (marker in this) {
        marker.map = null
    }
}