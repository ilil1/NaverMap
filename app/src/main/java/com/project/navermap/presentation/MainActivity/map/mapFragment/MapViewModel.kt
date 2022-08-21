package com.project.navermap.presentation.MainActivity.map.mapFragment

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.domain.model.FoodModel
import com.project.navermap.domain.usecase.mapViewmodel.GetItemsByRestaurantIdUseCase
import com.project.navermap.domain.usecase.restaurantListViewModel.GetRestaurantListUseCaseImpl
import com.project.navermap.domain.usecase.restaurantListViewModel.RestaurantResult
import com.project.navermap.presentation.MainActivity.store.restaurant.RestaurantCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel
@Inject
constructor(
    private val getRestaurantListUseCaseImpl: GetRestaurantListUseCaseImpl,
    private val getItemsByRestaurantIdUseCase: GetItemsByRestaurantIdUseCase,
) : ViewModel() {
    var filterCategoryChecked = mutableListOf<Boolean>()

    private val _data = MutableLiveData<MapState>(MapState.Uninitialized)
    val data: LiveData<MapState> get() = _data

    private val _items = MutableLiveData<List<FoodModel>>(emptyList())
    val items: LiveData<List<FoodModel>> get() = _items

    //상점을 외부DB로 부터 가져온다
    fun loadRestaurantList(
        restaurantCategory: RestaurantCategory,
        location: LocationEntity
    ) = viewModelScope.launch {
        when (val result = getRestaurantListUseCaseImpl.fetchData(
            restaurantCategory,
            location
        )) {
            is RestaurantResult.Success -> {
                _data.value = MapState.Success(result.data)
            }
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun loadRestaurantItems(
        restaurantId: Long
    ) = viewModelScope.launch {
        _items.value = getItemsByRestaurantIdUseCase(restaurantId)
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