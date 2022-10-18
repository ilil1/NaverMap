package com.project.navermap.domain.usecase.restaurantListViewModel

import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.data.entity.restaurant.RestaurantEntity
import com.project.navermap.data.repository.restaurant.RestaurantRepository
import com.project.navermap.domain.model.RestaurantModel
import com.project.navermap.presentation.mainActivity.store.restaurant.RestaurantCategory
import com.project.navermap.presentation.mainActivity.store.restaurant.RestautantFilterOrder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetRestaurantListUseCaseImpl(
    private val restaurantRepositoryImpl: RestaurantRepository,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun fetchData(
        restaurantCategory: RestaurantCategory,
        locationEntity: LocationEntity,
        filterOrder: RestautantFilterOrder = RestautantFilterOrder.DEFAULT
    ): RestaurantResult = withContext(ioDispatcher) {
        /* TODO: 2022-08-21 일 12:20, 리스트 가져오는데 실패하면 Failure로 return */
        val sortedList = sortList(
            restaurantRepositoryImpl.getList(restaurantCategory, locationEntity),
            filterOrder
        ).map { it.toRestaurantModel() }

        RestaurantResult.Success(data = sortedList)
    }

    private fun sortList(
        restaurantList: List<RestaurantEntity>,
        filterOrder: RestautantFilterOrder
    ) = when (filterOrder) {
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
}