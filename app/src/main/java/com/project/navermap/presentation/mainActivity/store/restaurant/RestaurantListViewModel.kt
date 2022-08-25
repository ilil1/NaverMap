package com.project.navermap.presentation.mainActivity.store.restaurant

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

class RestaurantListViewModel @AssistedInject constructor(
    private val getRestaurantListUseCaseImpl: GetRestaurantListUseCaseImpl,
    @Assisted private val restaurantCategory: RestaurantCategory,
    @Assisted private var locationEntity: LocationEntity
) : ViewModel() {

    /**
     * Hilt 런타임 주입
     */
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
        when (val result = getRestaurantListUseCaseImpl.fetchData(restaurantCategory, locationEntity)) {
            is RestaurantResult.Success -> {
//                val it = getRestaurantListUseCaseImpl.getRestaurantList()
                _restaurantListLiveData.value = result.data
            }
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
