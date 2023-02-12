package com.project.navermap.domain.usecase.restaurantListViewModel

import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.data.repository.restaurant.RestaurantRepository
import com.project.navermap.domain.model.RestaurantModel
import com.project.navermap.presentation.mainActivity.store.restaurant.RestaurantCategory
import com.project.navermap.presentation.mainActivity.store.restaurant.RestautantFilterOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class GetRestaurantListUseCase(
    private val restaurantRepository: RestaurantRepository,
) {

//상태관리와 함께 반환 해준다.
//    suspend fun fetchData(
//        restaurantCategory: RestaurantCategory,
//        locationEntity: LocationEntity,
//        filterOrder: RestautantFilterOrder = RestautantFilterOrder.DEFAULT
//    ): RestaurantResult {
//        return RestaurantResult.Success(
//            data = restaurantRepository.getList(restaurantCategory, locationEntity)
//                .sortList(filterOrder)
//        )
//    }

    suspend fun fetchData(
        restaurantCategory: RestaurantCategory,
        locationEntity: LocationEntity,
        filterOrder: RestautantFilterOrder = RestautantFilterOrder.DEFAULT
    ): Flow<List<RestaurantModel>> {
            return restaurantRepository.getList(restaurantCategory, locationEntity)
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