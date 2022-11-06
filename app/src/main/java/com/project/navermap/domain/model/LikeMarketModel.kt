package com.project.navermap.domain.model

import com.project.navermap.data.entity.LikeMarketEntity

data class LikeMarketModel(
    override val id: Long,
    val marketName: String,
    val distance: Float,
    val isOpen: Boolean,
    val imageUrl: String
) : Model(id, CellType.LIKE_MARKET_CELL) {
    companion object {
        fun fromEntity(entity: LikeMarketEntity) =
            LikeMarketModel(
                id = entity.id,
                marketName = entity.marketName,
                distance = entity.distance,
                isOpen = entity.isOpen,
                imageUrl = entity.imageUrl
            )
    }
}