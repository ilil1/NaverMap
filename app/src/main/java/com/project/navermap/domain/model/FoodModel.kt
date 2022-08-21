package com.project.navermap.domain.model

data class FoodModel(
    override val id: Long,
    override val type: CellType = CellType.FOOD_CELL,
    val title: String,
    val description: String,
    val price: String,
    val imageUrl: String,
    val restaurantId: Long
) : Model(id, type)
