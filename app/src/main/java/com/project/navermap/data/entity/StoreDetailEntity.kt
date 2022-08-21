package com.project.navermap.data.entity

import android.os.Parcelable
import com.project.navermap.domain.model.CellType
import com.project.navermap.presentation.MainActivity.store.restaurant.RestaurantCategory
import kotlinx.parcelize.Parcelize

@Parcelize
class StoreDetailEntity(
    val id: Long,
    val restaurantInfoId: Long,
    val restaurantCategory: RestaurantCategory,
    val restaurantTitle: String,
    val restaurantImageUrl: String,
    val grade: Float,
    val reviewCount: Int,
    val deliveryTimeRange: Pair<Int, Int>,
    val deliveryTipRange: Pair<Int, Int>,
    val restaurantTelNumber: String?,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
): Parcelable