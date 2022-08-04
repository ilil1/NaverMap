package com.project.navermap.domain.usecase.mapViewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons
import com.project.navermap.data.entity.ShopInfoEntity
import com.project.navermap.data.repository.ShopApiRepository
import com.project.navermap.presentation.MainActivity.map.mapFragment.MapState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

//일단은 이렇게 UseCase를 구성하지만 서버로 비즈니스 로직을 옮겨야함.
class GetUpdateMarkerUseCaseImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher
) {

    private var markers = mutableListOf<Marker>()

    private fun getCategoryNum(category: String): Int =
        when (category) {
            "FOOD_BEVERAGE" -> 0
            "SERVICE" -> 1
            "ACCESSORY" -> 2
            "MART" -> 3
            "FASHION" -> 4
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
        shopList: List<ShopInfoEntity>?) = withContext(ioDispatcher) {

        var temp = arrayListOf<Marker>()
        var i = 0

        shopList?.let {
            repeat(shopList.size) {
                if (filterCategoryChecked[getCategoryNum(shopList[i].category)]) {
                    temp += Marker().apply {
                        position = LatLng(shopList[i].latitude, shopList[i].longitude)
                        icon = MarkerIcons.BLACK
                        tag = shopList[i].shop_name
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