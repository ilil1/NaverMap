package com.project.navermap.data.repository.restaurant

import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.data.entity.restaurant.RestaurantEntity
import com.project.navermap.domain.model.FoodModel
import com.project.navermap.domain.model.RestaurantModel
import com.project.navermap.presentation.mainActivity.store.restaurant.RestaurantCategory
import kotlinx.coroutines.flow.Flow

interface RestaurantRepository {
    suspend fun getList(
        restaurantCategory: RestaurantCategory,
        locationLatLngEntity: LocationEntity
    ): List<RestaurantModel>

    suspend fun getItemsByRestaurantId(id: Long): List<FoodModel>
}
