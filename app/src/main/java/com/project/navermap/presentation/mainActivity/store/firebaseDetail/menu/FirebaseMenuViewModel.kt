package com.project.navermap.presentation.mainActivity.store.firebaseDetail.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.navermap.data.entity.firebase.ItemEntity
import com.project.navermap.data.repository.firebaserealtime.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class FirebaseMenuViewModel @Inject constructor(
    private val repository: ItemRepository
) : ViewModel() {

    private val _storeItem = MutableLiveData<List<ItemEntity>>(emptyList())
    val storeItem: LiveData<List<ItemEntity>> get() = _storeItem

    //추후에 Model을 분리해서 따로 관리
    fun loadRestaurantItems(restaurantId: String) = repository.getItemsByMarketId(restaurantId).onEach {
        _storeItem.value = it
    }.launchIn(viewModelScope)
}