package com.project.navermap.data.repository.home

import com.project.navermap.domain.model.FoodModel
import com.project.navermap.domain.model.RestaurantModel

interface HomeFirstMockRepository {
    fun getAllData() : List<RestaurantModel>
}