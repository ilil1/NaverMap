package com.project.navermap.data.datasource.restaurant

import com.project.navermap.data.response.restaurant.RestaurantFoodResponse

interface RestaurantDataSource {

    suspend fun getItemsByRestaurantId(id: Long): List<RestaurantFoodResponse>
}