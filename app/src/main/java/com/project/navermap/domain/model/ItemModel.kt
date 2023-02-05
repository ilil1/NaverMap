package com.project.navermap.domain.model

data class ItemModel(
    override val id: Long,
    val name: String,
    val originalPrice: Int,
    val discountedPrice: Int,
    val discountRatio: Int,
    val imageUrl: String? = null
) : Model(id, CellType.FIREBASE_ITEM_CELL)
