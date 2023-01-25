package com.project.navermap.data.entity.firebase

import android.os.Parcelable
import com.project.navermap.domain.model.CellType
import com.project.navermap.domain.model.FirebaseModel
import com.project.navermap.presentation.mainActivity.store.restaurant.RestaurantCategory
import kotlinx.parcelize.Parcelize

@Parcelize
data class FirebaseEntity(
    var restaurantId: String = "",
    var restaurantCategory: String = "",
    var restaurantTitle: String = "",
    var restaurantImageUrl: String = "",
    var grade: Float = 0.0f,
    var reviewCount: Int = 0,
    var restaurantTelNumber: String? = null,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var isMarketOpen: Boolean = true,
) : Parcelable {
    fun toModel() = FirebaseModel(
        restaurantId = restaurantId,
        restaurantCategory = RestaurantCategory.valueOf(restaurantCategory),
        restaurantTitle = restaurantTitle,
        restaurantImageUrl = restaurantImageUrl,
        grade = grade,
        reviewCount = reviewCount,
        restaurantTelNumber = restaurantTelNumber,
        latitude = latitude,
        longitude = longitude,
        isMarketOpen = isMarketOpen
    )
}
