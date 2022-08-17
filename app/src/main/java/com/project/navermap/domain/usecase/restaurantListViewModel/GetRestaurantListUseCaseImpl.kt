package com.project.navermap.domain.usecase.restaurantListViewModel

import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.data.repository.restaurant.RestaurantRepository
import com.project.navermap.domain.model.RestaurantModel
import com.project.navermap.presentation.MainActivity.store.restaurant.RestaurantCategory
import com.project.navermap.presentation.MainActivity.store.restaurant.RestautantFilterOrder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetRestaurantListUseCaseImpl(
    private val restaurantRepositoryImpl: RestaurantRepository,
    private val ioDispatcher: CoroutineDispatcher
) {
    private val _restaurantList: MutableList<RestaurantModel> = mutableListOf()

    suspend fun fetchData(
        restaurantCategory: RestaurantCategory,
        locationEntity: LocationEntity
    ) = withContext(ioDispatcher) {

        val restaurantList = restaurantRepositoryImpl.getList(restaurantCategory, locationEntity)

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

        sortedList.map {
            _restaurantList.add(
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
                    restaurantTelNumber = it.restaurantTelNumber,
                    latitude = it.latitude,
                    longitude = it.longitude
                )
            )
        }

        RestaurantResult.Success(data = _restaurantList)
    }
}