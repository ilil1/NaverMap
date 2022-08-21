package com.project.navermap.domain.usecase.StoreViewModel

import com.project.navermap.data.entity.restaurant.RestaurantEntity

sealed class StoreDetailResult {

    object Uninitialized: StoreDetailResult()

    object Loading : StoreDetailResult()

    data class Success(
        val restaurantEntity: RestaurantEntity
    ): StoreDetailResult()

}