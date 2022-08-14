package com.project.navermap.domain.usecase.restaurantListViewModel

import com.project.navermap.domain.usecase.mapViewmodel.ShopResult

sealed class RestaurantResult {
    object Success : RestaurantResult()
    object Failure : RestaurantResult()
}
