package com.project.navermap.domain.model

import com.project.navermap.data.entity.SuggestItemEntity
import com.project.navermap.data.entity.restaurant.RestaurantEntity
import com.project.navermap.domain.model.category.SuggestCategory

data class SuggestItemModel(
    override val id: Long,
    override val type: CellType = CellType.SUGGEST_CELL,
    val marketImageUrl : String,
    val marketName: String,
    val distance:Float,
    val category: SuggestCategory
):Model(id, type) {

    fun toEntity() = SuggestItemEntity(
        id,
        type,
        marketImageUrl,
        marketName,
        distance,
        category
    )
}