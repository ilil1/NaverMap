package com.project.navermap.presentation.mainActivity.store.restaurant

import androidx.lifecycle.*
import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.data.repository.firebaserealtime.FirebaseRepository
import com.project.navermap.domain.model.FirebaseModel
import com.project.navermap.domain.usecase.restaurantListViewModel.GetRestaurantListUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RestaurantListViewModel @AssistedInject constructor(
    private val getRestaurantListUseCase: GetRestaurantListUseCase,
    private val firebaseRepository: FirebaseRepository,
    @Assisted private val restaurantCategory: RestaurantCategory,
    @Assisted private var locationEntity: LocationEntity
) : ViewModel() {

    /**
     * Hilt 런타임 주입
     */
    @AssistedFactory
    interface RestaurantAssistedFactory {
        fun create(
            restaurantCategory: RestaurantCategory,
            locationEntity: LocationEntity
        ): RestaurantListViewModel
    }

//    @AssistedFactory
//    interface StoreAssistedFactory {
//        fun create(storeCategory: StoreCategory,
//                   locationEntity: LocationEntity): RestaurantListViewModel
//    }

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

//        fun provideStoreFactory(
//            assistedFactory: StoreAssistedFactory,
//            storeCategory: StoreCategory,
//            locationEntity: LocationEntity
//        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
//            @Suppress("UNCHECKED_CAST")
//            override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                return assistedFactory.create(storeCategory, locationEntity) as T
//            }
//        }
    }

//    private var _restaurantListLiveData = MutableLiveData<List<RestaurantModel>>()
    val restaurantListLiveData: LiveData<List<FirebaseModel>> = firebaseRepository.getMarkets()
        .map { list -> list.map { entity -> entity.toModel() } }
        .asLiveData()

    fun fetchData(): Job = viewModelScope.launch {
//        getRestaurantListUseCase.fetchData(restaurantCategory, locationEntity).let {
//            when (it) {
//                is RestaurantResult.Success -> _restaurantListLiveData.value = it.data
//                else -> _restaurantListLiveData.value = emptyList() /* TODO: 2022-09-20 화 00:33, Error 구현 */
//            }
//        }
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
