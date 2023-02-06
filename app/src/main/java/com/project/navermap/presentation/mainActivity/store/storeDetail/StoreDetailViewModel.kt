package com.project.navermap.presentation.mainActivity.store.storeDetail

import androidx.lifecycle.*
import com.project.navermap.data.entity.restaurant.RestaurantEntity
import com.project.navermap.domain.usecase.mapViewmodel.GetItemsByRestaurantIdUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class StoreDetailViewModel @AssistedInject constructor(
    @Assisted private val restaurantEntity: RestaurantEntity,
): ViewModel() {

    /**
     * Hilt 런타임 주입
     */
    @AssistedFactory
    interface StoreDetailAssistedFactory {
        fun create(restaurantEntity: RestaurantEntity
        ): StoreDetailViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: StoreDetailAssistedFactory,
            restaurantEntity: RestaurantEntity
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(restaurantEntity) as T
            }
        }
    }

    private val _storeDetailResultLiveData = MutableLiveData<StoreDetailResult>(StoreDetailResult.Uninitialized)
    val storeDetailResultLiveData: LiveData<StoreDetailResult> get() = _storeDetailResultLiveData

    fun fetchData(): Job = viewModelScope.launch {
        _storeDetailResultLiveData.value = StoreDetailResult.Loading
        _storeDetailResultLiveData.value = StoreDetailResult.Success(
            restaurantEntity = restaurantEntity
        )
    }
}