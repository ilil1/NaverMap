package com.project.navermap.presentation.mainActivity.home.suggestItemDetail

import com.project.navermap.data.entity.SuggestItemEntity
import com.project.navermap.data.entity.restaurant.RestaurantEntity
import com.project.navermap.presentation.mainActivity.store.storeDetail.StoreDetailResult

sealed class SuggestItemDetailResult {
    object Uninitialized: SuggestItemDetailResult()

    object Loading : SuggestItemDetailResult()

    data class Success(
        val suggestItemEntity: SuggestItemEntity
    ): SuggestItemDetailResult()
}
