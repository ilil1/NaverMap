package com.project.navermap.presentation.mainActivity.map.mapFragment

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.domain.model.FoodModel
import com.project.navermap.domain.model.RestaurantModel
import com.project.navermap.domain.usecase.mapViewmodel.GetItemsByRestaurantIdUseCase
import com.project.navermap.domain.usecase.restaurantListViewModel.GetRestaurantListUseCase
import com.project.navermap.presentation.base.UiState
import com.project.navermap.presentation.mainActivity.store.restaurant.RestaurantCategory
import com.project.navermap.presentation.mainActivity.store.restaurant.RestautantFilterOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getRestaurantListUseCase: GetRestaurantListUseCase,
    private val getItemsByRestaurantIdUseCase: GetItemsByRestaurantIdUseCase
) : ViewModel() {

    lateinit var destLocation: LocationEntity
    var filterCategoryChecked = mutableListOf<Boolean>()

    private val _data = MutableLiveData<MapState>(MapState.Uninitialized)
    val data: LiveData<MapState> get() = _data

    var _mapDataState: MutableSharedFlow<UiState<MapState>> = MutableSharedFlow()
    val mapDataState: SharedFlow<UiState<MapState>> = _mapDataState

    private val _items = MutableLiveData<List<FoodModel>>(emptyList())
    val items: LiveData<List<FoodModel>> get() = _items

    private var restaurantList: MutableList<RestaurantModel> = mutableListOf()

    /**
    백엔드 API를 좀더 유동적으로 사용할수있게 쿼리를 알아봐야함 현재는 필터 구현을 위한 임시방편
     */
//    fun loadRestaurantList(
//        restaurantCategory: RestaurantCategory,
//        location: LocationEntity = destLocation
//    ) = viewModelScope.launch {
//        restaurantList.clear()
//        val restaurantCategories = RestaurantCategory.values()
//        restaurantCategories.map {
//            getRestaurantListUseCase.fetchData(it, location).let {
//                /* TODO: 2022-09-20 화 00:34, Error 구현 */
//                when (it) {
//                    is RestaurantResult.Success -> {
//                        val mutableResult = it.data.toMutableList()
//                        var i = 0
//                        repeat(mutableResult.size) {
//                            if (filterCategoryChecked[getCategoryNum(mutableResult[i].restaurantCategory.toString())]) {
//                                restaurantList.add(mutableResult[i])
//                            }
//                            i++
//                        }
//                        _data.value = MapState.Success(restaurantList)
//                    }
//                }
//            }
//        }
//    }

    fun loadRestaurantList(
        restaurantCategory: RestaurantCategory,
        location: LocationEntity = destLocation
    ) = viewModelScope.launch {
        restaurantList.clear()
        val restaurantCategories = RestaurantCategory.values()
        restaurantCategories.map {
            getRestaurantListUseCase.fetchData(it, location)
                .onStart {
                    _mapDataState.emit(UiState.Loading(true))
                }.onEach {
                    val mutableResult = it.toMutableList()
                    var i = 0
                    repeat(mutableResult.size) {
                        if (filterCategoryChecked[
                                    getCategoryNum(mutableResult[i].restaurantCategory.toString())]
                        ) {
                            restaurantList.add(mutableResult[i])
                        }
                        i++
                    }
                    _mapDataState.emit(UiState.Success(MapState.Success(restaurantList)))
                }.catch {
                    _mapDataState.emit(UiState.Fail(null))
                }.onCompletion {
                    _mapDataState.emit(UiState.Loading(false))
                }.launchIn(viewModelScope)
        }
    }

    private fun List<RestaurantModel>.sortList(
        filterOrder: RestautantFilterOrder
    ) = when (filterOrder) {
        RestautantFilterOrder.DEFAULT -> {
            this
        }
        RestautantFilterOrder.LOW_DELIVERY_TIP -> {
            sortedBy { it.deliveryTipRange.first }
        }
        RestautantFilterOrder.FAST_DELIVERY -> {
            sortedBy { it.deliveryTimeRange.first }
        }
        RestautantFilterOrder.TOP_RATE -> {
            sortedByDescending { it.grade }
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun loadRestaurantItems(
        restaurantId: Long
    ) = viewModelScope.launch {

        //_items.value = getItemsByRestaurantIdUseCase(restaurantId)
        getItemsByRestaurantIdUseCase(restaurantId).onStart {

        }.onEach {
            _items.value = it
        }.onCompletion {

        }.catch {

        }.launchIn(viewModelScope)
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
}