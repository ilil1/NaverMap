package com.project.navermap.domain.model

data class FoodModel(
    override val id: Long,
    val title: String,
    val description: String,
    val price: String,
    val imageUrl: String,
    val restaurantId: Long
) : Model(id, CellType.FOOD_CELL)
