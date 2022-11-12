package com.project.navermap.presentation.mainActivity.store.firebaseDetail

import androidx.lifecycle.*
import com.project.navermap.data.entity.firebase.FirebaseEntity
import com.project.navermap.data.entity.restaurant.RestaurantEntity
import com.project.navermap.domain.usecase.mapViewmodel.GetItemsByRestaurantIdUseCase
import com.project.navermap.presentation.mainActivity.store.storeDetail.StoreDetailResult
import com.project.navermap.presentation.mainActivity.store.storeDetail.StoreDetailViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FirebaseDetailViewModel @AssistedInject constructor(
    @Assisted private val firebaseEntity: FirebaseEntity,
    private val getItemsByRestaurantIdUseCase: GetItemsByRestaurantIdUseCase
): ViewModel() {

    /**
     * Hilt 런타임 주입
     */
    @AssistedFactory
    interface FirebaseDetailAssistedFactory {
        fun create(restaurantEntity: RestaurantEntity
        ): FirebaseDetailViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: FirebaseDetailAssistedFactory,
            restaurantEntity: RestaurantEntity
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(restaurantEntity) as T
            }
        }
    }

    private val _storeDetailResultLiveData = MutableLiveData<FirebaseDetailResult>(FirebaseDetailResult.Uninitialized)
    val storeDetailResultLiveData: LiveData<FirebaseDetailResult> get() = _storeDetailResultLiveData

    fun fetchData(): Job = viewModelScope.launch {
        _storeDetailResultLiveData.value = FirebaseDetailResult.Loading
        _storeDetailResultLiveData.value = FirebaseDetailResult.Success(
            firebaseEntity = firebaseEntity
        )
    }
}