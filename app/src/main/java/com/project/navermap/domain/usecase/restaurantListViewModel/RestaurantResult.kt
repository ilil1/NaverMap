package com.project.navermap.domain.usecase.restaurantListViewModel

import com.project.navermap.domain.model.RestaurantModel

sealed class RestaurantResult {
    data class Success(val data: List<RestaurantModel>) : RestaurantResult()
    object Failure : RestaurantResult()
}
