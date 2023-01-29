package com.project.navermap.data.entity.restaurant

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.project.navermap.domain.model.CellType
import com.project.navermap.domain.model.Model
import com.project.navermap.domain.model.RestaurantModel
import com.project.navermap.domain.model.StoreModel
import com.project.navermap.presentation.mainActivity.store.restaurant.RestaurantCategory
import com.project.navermap.presentation.mainActivity.store.restaurant.StoreCategory
import kotlinx.parcelize.Parcelize

// TODO: Entity에 프레임워크를 달아야한다
@Parcelize
data class StoreEntity(
    val id: Long,
    val storeInfoId: Long,//api호출용도
    val storeCategory: StoreCategory,
    @PrimaryKey
    val storeTitle: String,
    val storeImageUrl: String,
    val grade: Float,
    val reviewCount: Int,
    val deliveryTimeRange: Pair<Int, Int>,
    val deliveryTipRange: Pair<Int, Int>,
    val restaurantTelNumber: String?,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
) : Parcelable

