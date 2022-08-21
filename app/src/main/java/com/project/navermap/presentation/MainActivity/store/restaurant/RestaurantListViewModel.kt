package com.project.navermap.presentation.MainActivity.store.restaurant

import androidx.lifecycle.*
import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.domain.model.RestaurantModel
import com.project.navermap.domain.usecase.restaurantListViewModel.GetRestaurantListUseCaseImpl
import com.project.navermap.domain.usecase.restaurantListViewModel.RestaurantResult
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/* TODO: 2022-08-21 일 12:23, category와 location entity constructor에서 제거 */
class RestaurantListViewModel @AssistedInject constructor(
    private val getRestaurantListUseCaseImpl: GetRestaurantListUseCaseImpl,
    @Assisted private val restaurantCategory: RestaurantCategory,
    @Assisted private var locationEntity: LocationEntity
) : ViewModel() {

    @AssistedFactory
    interface RestaurantAssistedFactory {
        fun create(restaurantCategory: RestaurantCategory,
                   locationEntity: LocationEntity): RestaurantListViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: RestaurantAssistedFactory,
            restaurantCategory: RestaurantCategory,
            locationEntity: LocationEntity
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(restaurantCategory, locationEntity) as T
            }
        }
    }

    private var _restaurantListLiveData = MutableLiveData<List<RestaurantModel>>()
    val restaurantListLiveData: LiveData<List<RestaurantModel>> get() = _restaurantListLiveData

    fun fetchData(): Job = viewModelScope.launch {
        when (val result = getRestaurantListUseCaseImpl.fetchData(
            restaurantCategory,
            locationEntity
        )) {
            is RestaurantResult.Success -> {
                _restaurantListLiveData.value = result.data
            }

            RestaurantResult.Failure -> TODO("error state 처리")
        }
    }

    fun setLocationLatLng(locationEntity: LocationEntity) {
        this.locationEntity = locationEntity
        fetchData()
    }

    fun setRestaurantFilterOrder(order: RestautantFilterOrder) {
        //this.restaurantFilterOrder = order
        fetchData()
    }

}
