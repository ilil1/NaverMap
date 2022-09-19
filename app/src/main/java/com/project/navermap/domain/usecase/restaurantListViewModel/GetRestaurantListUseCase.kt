package com.project.navermap.domain.usecase.restaurantListViewModel

import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.data.repository.restaurant.RestaurantRepository
import com.project.navermap.domain.model.RestaurantModel
import com.project.navermap.presentation.mainActivity.store.restaurant.RestaurantCategory
import com.project.navermap.presentation.mainActivity.store.restaurant.RestautantFilterOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

class GetRestaurantListUseCase(
    private val restaurantRepository: RestaurantRepository,
) {
    fun fetchData(
        restaurantCategory: RestaurantCategory,
        locationEntity: LocationEntity,
        filterOrder: RestautantFilterOrder = RestautantFilterOrder.DEFAULT
    ): Flow<RestaurantResult> {
        return restaurantRepository.getList(restaurantCategory, locationEntity)
            .transform {
                emit(RestaurantResult.Success(data = it.sortList(filterOrder)))
            }
    }


    private fun List<RestaurantModel>.sortList(
        filterOrder: RestautantFilterOrder
    ) = when (filterOrder) {
        RestautantFilterOrder.DEFAULT -> {
            this
        }
        RestautantFilterOrder.LOW_DELIVERY_TIP -> {
            sortedBy { it.deliveryTipRange.first }
        }
        RestautantFilterOrder.FAST_DELIVERY -> {
            sortedBy { it.deliveryTimeRange.first }
        }
        RestautantFilterOrder.TOP_RATE -> {
            sortedByDescending { it.grade }
        }
    }
}