package com.project.navermap.data.entity.firebase

import com.project.navermap.domain.model.ItemModel

// CRUD
data class ItemEntity(
    var id: Long = 0L, // not updatable
    var name: String = "",
    var description: String = "",
    var stock: Int = 0,
    var price: Int = 0,
    var discountedPrice: Int = 0,
    var imageUrl: String? = null,
    var optionGroups: List<OptionGroupEntity> = emptyList(),
    var available: Boolean = false
) {
    fun toModel() = ItemModel(
        id = id,
        name = name,
        originalPrice = price,
        discountedPrice = discountedPrice,
        discountRatio = (price - discountedPrice) / price,
        imageUrl = imageUrl
    )
}

data class OptionGroupEntity(
    var id: Long = 0L,
    var name: String = "",
    var options: List<OptionEntity> = emptyList(),
    var minSelect: Int = 0,
    var maxSelect: Int = 0
)

data class OptionEntity(
    var id: Long = 0L,
    var name: String = "",
    var additionalPrice: Int = 0
)