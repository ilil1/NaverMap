package com.project.navermap.data.response.restaurant

import com.project.navermap.domain.model.FoodModel

data class RestaurantFoodResponse(
    val id: String,
    val title: String,
    val description: String,
    val price: String,
    val imageUrl: String
) {
    fun toModel(restaurantId: Long) = FoodModel(
        id = id.toLong(),
        title = title,
        description = description,
        price = price,
        imageUrl = imageUrl,
        restaurantId = restaurantId
    )
}
