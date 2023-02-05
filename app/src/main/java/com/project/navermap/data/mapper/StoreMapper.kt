package com.project.navermap.data.mapper

import com.project.navermap.data.entity.restaurant.StoreEntity
import com.project.navermap.domain.model.CellType
import com.project.navermap.domain.model.StoreModel

fun StoreEntity.toStoreModel() = StoreModel(
    id = id,
    type = CellType.STORE_CELL,
    storeInfoId = storeInfoId,
    storeCategory = storeCategory,
    storeTitle = storeTitle,
    storeImageUrl = storeImageUrl,
    grade = grade,
    reviewCount = reviewCount,
    deliveryTimeRange = deliveryTimeRange,
    deliveryTipRange = deliveryTipRange,
    storeTelNumber = restaurantTelNumber,
    latitude = latitude,
    longitude = longitude
)