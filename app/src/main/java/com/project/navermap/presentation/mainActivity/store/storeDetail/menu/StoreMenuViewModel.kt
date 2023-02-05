package com.project.navermap.presentation.mainActivity.store.storeDetail.menu


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.navermap.domain.model.CellType
import com.project.navermap.domain.model.FoodModel
import com.project.navermap.domain.usecase.mapViewmodel.GetItemsByRestaurantIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreMenuViewModel @Inject constructor(
    private val getItemsByRestaurantIdUseCase: GetItemsByRestaurantIdUseCase,
) : ViewModel() {

//    private val _storeItem = MutableLiveData<List<FoodModel>>(emptyList())
//    val storeItem: LiveData<List<FoodModel>> get() = _storeItem

    private var _storeItem: MutableStateFlow<List<FoodModel>> = MutableStateFlow(emptyList())
    val storeItem: StateFlow<List<FoodModel>> = _storeItem

//    fun loadRestaurantItems(restaurantId: Long) = viewModelScope.launch {
//        _storeItem.value = getItemsByRestaurantIdUseCase(restaurantId).map {
//            it.copy(type = CellType.STORE_DETAIL_FOOD_CELL)
//        }
//    }

    //추후에 Model을 분리해서 따로 관리
    fun loadRestaurantItems(restaurantId: Long) = viewModelScope.launch {
        getItemsByRestaurantIdUseCase(restaurantId)
            .onStart {

            }.onEach {
                _storeItem.value = it.map {
                    it.copy(type = CellType.STORE_DETAIL_FOOD_CELL)
                }
            }.onCompletion {

            }.catch {

            }.launchIn(viewModelScope)
    }
}