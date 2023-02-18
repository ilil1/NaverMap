package com.project.navermap.presentation.mainActivity.map.mapFragment

import androidx.annotation.StringRes
import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.domain.model.RestaurantModel
import com.project.navermap.presentation.base.UiState


sealed class MapState {
    object Uninitialized : MapState()

    data class Loading(val data: Boolean): MapState()

    data class Success(
        val restaurantInfoList: MutableList<RestaurantModel>,
        val destLocation: LocationEntity? = null
    ) : MapState()
//
//    data class Error(
//        @StringRes val id: Int
//    ) : MapState()

    object Error : MapState()
}
