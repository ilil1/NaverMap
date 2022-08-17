package com.project.navermap.domain.usecase.mapViewmodel

import com.naver.maps.map.overlay.Marker

sealed class MarkerResult {
    data class Success(val markers: List<Marker>) : MarkerResult()
    object Failure : MarkerResult()
}
