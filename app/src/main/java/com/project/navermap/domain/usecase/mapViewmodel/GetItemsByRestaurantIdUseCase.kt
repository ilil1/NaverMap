package com.project.navermap.domain.usecase.mapViewmodel

import com.project.navermap.data.repository.restaurant.RestaurantRepository
import com.project.navermap.domain.model.FoodModel
import javax.inject.Inject

class GetItemsByRestaurantIdUseCase(
    private val repository: RestaurantRepository
) {
    suspend operator fun invoke(id: Long): List<FoodModel> =
        repository.getItemsByRestaurantId(id % 10 + 1) // 1 ~ 100000 -> 1 ~ 10
}
