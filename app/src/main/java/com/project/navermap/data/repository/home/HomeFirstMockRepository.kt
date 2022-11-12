package com.project.navermap.data.repository.home

import com.project.navermap.domain.model.FoodModel

interface HomeFirstMockRepository {
    fun getAllData() : List<FoodModel>
}