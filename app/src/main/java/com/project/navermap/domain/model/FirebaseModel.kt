package com.project.navermap.domain.model

import com.project.navermap.data.entity.TownMarketEntity
import com.project.navermap.data.entity.firebase.FirebaseEntity
import com.project.navermap.presentation.mainActivity.store.restaurant.RestaurantCategory

data class FirebaseModel(
    override val id: Long = 0L,
    override val type : CellType = CellType.FIREBASE_CELL,
    val restaurantId: String,
    val restaurantCategory: RestaurantCategory,
    val restaurantTitle: String,
    val restaurantImageUrl: String,
    val grade: Float,
    val reviewCount: Int,
    val restaurantTelNumber: String?,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    val isMarketOpen: Boolean,
): Model(id, type)
{
    override fun isTheSame(item: Model): Boolean = if (item is FirebaseModel) {
        super.isTheSame(item) && item.restaurantId == this.restaurantId
    } else {
        false
    }

    fun toEntity() = FirebaseEntity(
        restaurantId,
        restaurantCategory.name,
        restaurantTitle,
        restaurantImageUrl,
        grade,
        reviewCount,
        restaurantTelNumber,
        latitude,
        longitude,
        isMarketOpen
    )
}
