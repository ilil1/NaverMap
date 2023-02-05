package com.project.navermap.data.datasource.restaurant

import com.project.navermap.data.response.restaurant.RestaurantFoodResponse
import kotlinx.coroutines.flow.Flow

interface RestaurantDataSource {

    //suspend fun getItemsByRestaurantId(id: Long): List<RestaurantFoodResponse>

    suspend fun getItemsByRestaurantId(id: Long): Flow<List<RestaurantFoodResponse>>
}