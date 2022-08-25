package com.project.navermap.presentation.mainActivity.store.storeDetail

import com.project.navermap.data.entity.restaurant.RestaurantEntity

sealed class StoreDetailResult {

    object Uninitialized: StoreDetailResult()

    object Loading : StoreDetailResult()

    data class Success(
        val restaurantEntity: RestaurantEntity
    ): StoreDetailResult()

}