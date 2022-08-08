package com.project.navermap.data.entity.restaurant

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.project.navermap.presentation.MainActivity.store.restaurant.RestaurantCategory
import kotlinx.parcelize.Parcelize

@Parcelize
data class RestaurantEntity(
    val id: Long,
    val restaurantInfoId: Long,//api호출용도
    val restaurantCategory: RestaurantCategory,
    @PrimaryKey val restaurantTitle: String,
    val restaurantImageUrl: String,
    val grade: Float,
    val reviewCount: Int,
    val deliveryTimeRange: Pair<Int, Int>,
    val deliveryTipRange: Pair<Int, Int>,
    val restaurantTelNumber: String?
):  Parcelable
