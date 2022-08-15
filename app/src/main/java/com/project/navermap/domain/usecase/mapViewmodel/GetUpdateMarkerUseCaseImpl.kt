package com.project.navermap.domain.usecase.mapViewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons
import com.project.navermap.data.entity.ShopInfoEntity
import com.project.navermap.data.repository.ShopApiRepository
import com.project.navermap.domain.model.RestaurantModel
import com.project.navermap.presentation.MainActivity.map.mapFragment.MapState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetUpdateMarkerUseCaseImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher
) {

    private var markers = mutableListOf<Marker>()

    private fun getCategoryNum(category: String): Int =
        when (category) {
            "ALL" -> 0
            "KOREAN_FOOD" -> 1
            "DUMPLING_FOOD" -> 2
            "CAFE_DESSERT" -> 3
            "JAPANESE_FOOD" -> 4
            else -> 5
        }

    fun setMarkers(list: List<Marker>) {
        markers.clear()
        markers = list as MutableList
    }

    fun getMarkers() : List<Marker> {
        return markers
    }

    suspend fun updateMarker(
        filterCategoryChecked : MutableList<Boolean>,
        restaurantList: List<RestaurantModel>?) = withContext(ioDispatcher) {

        var temp = arrayListOf<Marker>()
        var i = 0

        restaurantList?.let {
            repeat(restaurantList.size) {
                if (filterCategoryChecked[getCategoryNum(restaurantList[i].restaurantCategory.toString())]) {
                    temp += Marker().apply {
                        position = LatLng(restaurantList[i].latitude, restaurantList[i].longitude)
                        icon = MarkerIcons.BLACK
                        tag = restaurantList[i].restaurantTitle
                        zIndex = i
                    }
                }
                i++
            }
            setMarkers(temp)
        }
        MarkerResult.Success
    }
}