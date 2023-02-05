package com.project.navermap.domain.usecase.mapViewmodel

import android.content.ContentValues.TAG
import android.util.Log
import com.project.navermap.data.repository.restaurant.RestaurantRepository
import com.project.navermap.domain.model.FoodModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetItemsByRestaurantIdUseCase(
    private val repository: RestaurantRepository
) {
//코루틴
//    suspend operator fun invoke(id: Long): List<FoodModel> =
//        repository.getItemsByRestaurantId(id % 10 + 1) // 1 ~ 100000 -> 1 ~ 10

    //플로우
    suspend operator fun invoke(id: Long): Flow<List<FoodModel>> {
        return repository.getItemsByRestaurantId(id % 10 + 1) // 1 ~ 100000 -> 1 ~ 10
    }
}
