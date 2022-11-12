package com.project.navermap.domain.model

import com.project.navermap.data.entity.ShopInfoEntity

data class LikeModel(
    override val id: Long,
    val shop_name: String,
    val is_open: Boolean,
    val average_score: Int,
    val review_number: Int
) : Model(id, CellType.STORE_CELL) {
    companion object {
        fun fromEntity(entity: ShopInfoEntity) =
            LikeModel(
                id = entity.shop_id.toLong(),
                shop_name = entity.shop_name,
                is_open = entity.is_open,
                average_score = entity.average_score,
                review_number = entity.review_number
            )
    }
}