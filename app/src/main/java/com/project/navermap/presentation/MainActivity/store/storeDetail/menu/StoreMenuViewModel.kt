package com.project.navermap.presentation.MainActivity.store.storeDetail.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.navermap.domain.model.CellType
import com.project.navermap.domain.model.FoodModel
import com.project.navermap.domain.usecase.mapViewmodel.GetItemsByRestaurantIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreMenuViewModel @Inject constructor(
    private val getItemsByRestaurantIdUseCase: GetItemsByRestaurantIdUseCase,
) : ViewModel() {


//    lateinit var allNewSaleItemList : List<FoodModel>

//    fun fetchData(): Job = viewModelScope.launch {
//        fetchItemData()
//    }

//    private suspend fun fetchItemData() {
//        if(items.value is StoreMenuState.Success<*>) {
//            _items.value = StoreMenuState.Loading
//
//            allNewSaleItemList =
//
//
//        }
//    }

    private val _items = MutableLiveData<StoreMenuState>(StoreMenuState.Uninitialized)
    val items: LiveData<StoreMenuState> get() = _items

    private val _storeItem = MutableLiveData<List<FoodModel>>(emptyList())
    val storeItem : LiveData<List<FoodModel>> get() = _storeItem


    fun fetchData(){
        if(items.value is StoreMenuState.Uninitialized){
            //_items.value = StoreMenuState.Loading
        }
    }


    fun loadRestaurantItems(
        restaurantId:Long
    ) = viewModelScope.launch {
        _storeItem.value = getItemsByRestaurantIdUseCase(restaurantId).map {
            it.copy(type = CellType.STORE_DETAIL_FOOD_CELL)
        }
    }




}