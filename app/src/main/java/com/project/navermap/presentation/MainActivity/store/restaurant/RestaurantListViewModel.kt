package com.project.navermap.presentation.MainActivity.store.restaurant

import android.content.Context
import android.content.ContextWrapper
import androidx.lifecycle.*
import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.data.repository.MapApiRepository
import com.project.navermap.data.repository.restaurant.RestaurantRepository
import com.project.navermap.domain.model.RestaurantModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class RestaurantListViewModel @AssistedInject constructor(
    @Assisted private val restaurantCategory: RestaurantCategory,
    @Assisted private var locationEntity: LocationEntity,
    private val restaurantRepository: RestaurantRepository
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
        val restaurantList = restaurantRepository.getList(restaurantCategory, locationEntity)

        val sortedList = when (RestautantFilterOrder.DEFAULT) {
            RestautantFilterOrder.DEFAULT -> {
                restaurantList
            }
            RestautantFilterOrder.LOW_DELIVERY_TIP -> {
                restaurantList.sortedBy { it.deliveryTipRange.first }
            }
            RestautantFilterOrder.FAST_DELIVERY -> {
                restaurantList.sortedBy { it.deliveryTimeRange.first }
            }
            RestautantFilterOrder.TOP_RATE -> {
                restaurantList.sortedByDescending { it.grade }
            }
        }

        _restaurantListLiveData.value = sortedList.map {
            RestaurantModel(
                id = it.id,
                restaurantInfoId = it.restaurantInfoId,
                restaurantCategory = it.restaurantCategory,
                restaurantTitle = it.restaurantTitle,
                restaurantImageUrl = it.restaurantImageUrl,
                grade = it.grade,
                reviewCount = it.reviewCount,
                deliveryTimeRange = it.deliveryTimeRange,
                deliveryTipRange = it.deliveryTipRange,
                restaurantTelNumber = it.restaurantTelNumber
            )
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
