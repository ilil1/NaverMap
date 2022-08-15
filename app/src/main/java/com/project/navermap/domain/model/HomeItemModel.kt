package com.project.navermap.domain.model

import com.project.navermap.domain.model.category.HomeListCategory
import com.project.navermap.domain.model.category.HomeListDetailCategory

data class HomeItemModel(
    override val id: Long,
    val homeListCategory: HomeListCategory,
    val homeListDetailCategory: HomeListDetailCategory,
    val itemImageUrl: String,
    val townMarketModel: TownMarketModel,
    val itemName: String,
    val originalPrice: Int,
    val salePrice: Int,
    val stockQuantity: Int,
    val likeQuantity: Int,
    val reviewQuantity: Int,
    override val type: CellType = CellType.HOME_ITEM_CELL
): Model(id, type) {

    override fun isTheSame(item: Model) =
        if (item is HomeItemModel) {
            super.isTheSame(item) && this.homeListCategory == item.homeListCategory
        } else {
            false
        }
}