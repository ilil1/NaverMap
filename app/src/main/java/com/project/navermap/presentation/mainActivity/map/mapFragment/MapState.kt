package com.project.navermap.presentation.mainActivity.map.mapFragment

import androidx.annotation.StringRes
import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.domain.model.RestaurantModel



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
    data class Fail(val data : String?): MapState()
    object Error : MapState()
}

fun MapState.successOrNull(): Pair<MutableList<RestaurantModel>, LocationEntity?>? {
    return if (this is MapState.Success) {
        Pair(restaurantInfoList, destLocation)
    } else {
        null
    }
}

fun MapState.failOrNull(): String? {
    return if (this is MapState.Fail) {
        data
    } else {
        null
    }
}
