package com.project.navermap.presentation.mainActivity.map.SearchAddress

import androidx.annotation.StringRes
import com.project.navermap.data.entity.MapSearchInfoEntity

sealed class SearchState {

    object Uninitialized : SearchState()
    object Loading : SearchState()

    data class Success(
        val mapSearchInfoEntity: MapSearchInfoEntity,
//        val foodMenuListInBasket: List<RestaurantFoodEntity>? = null
    ) : SearchState()

    data class Error(
        @StringRes val errorMessage: Int
    ) : SearchState()
}
