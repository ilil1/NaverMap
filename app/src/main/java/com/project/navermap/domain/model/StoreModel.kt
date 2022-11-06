package com.project.navermap.domain.model

import com.project.navermap.data.entity.restaurant.RestaurantEntity
import com.project.navermap.data.entity.restaurant.StoreEntity
import com.project.navermap.presentation.mainActivity.store.restaurant.RestaurantCategory
import com.project.navermap.presentation.mainActivity.store.restaurant.StoreCategory

data class StoreModel(
    override val id: Long,
    override val type: CellType = CellType.STORE_CELL,
    val storeInfoId: Long,
    val storeCategory: StoreCategory,
    val storeTitle: String,
    val storeImageUrl: String,
    val grade: Float,
    val reviewCount: Int,
    val deliveryTimeRange: Pair<Int, Int>,
    val deliveryTipRange: Pair<Int, Int>,
    val storeTelNumber: String?,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
) : Model(id, type) {
    fun toEntity() = StoreEntity(
        id,
        storeInfoId,
        storeCategory,
        storeTitle,
        storeImageUrl,
        grade,
        reviewCount,
        deliveryTimeRange,
        deliveryTipRange,
        storeTelNumber
    )
}
