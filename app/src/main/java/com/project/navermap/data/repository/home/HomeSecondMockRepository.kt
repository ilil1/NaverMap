package com.project.navermap.data.repository.home

import com.project.navermap.domain.model.FoodModel

interface HomeSecondMockRepository {
    fun getAllData() : List<FoodModel>
}