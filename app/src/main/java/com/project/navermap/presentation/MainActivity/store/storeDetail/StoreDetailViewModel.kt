package com.project.navermap.presentation.MainActivity.store.storeDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.navermap.domain.model.FoodModel
import com.project.navermap.domain.usecase.StoreViewModel.StoreDetailResult
import com.project.navermap.domain.usecase.mapViewmodel.GetItemsByRestaurantIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreDetailViewModel @Inject constructor(
    private val getItemsByRestaurantIdUseCase: GetItemsByRestaurantIdUseCase
): ViewModel() {

    private val _items = MutableLiveData<StoreDetailResult>(StoreDetailResult.Uninitialized)
    val items: LiveData<StoreDetailResult> get() = _items


//    fun loadRestaurantItems(
//        restaurantId:Long
//    ) = viewModelScope.launch {
//        _items.value =getItemsByRestaurantIdUseCase(restaurantId)
//    }


    fun fetchData(): Job = viewModelScope.launch {
        _items.value = StoreDetailResult.Loading
    }

}