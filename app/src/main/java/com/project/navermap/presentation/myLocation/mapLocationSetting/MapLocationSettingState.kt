package com.project.navermap.presentation.myLocation.mapLocationSetting

import androidx.annotation.StringRes
import com.project.navermap.data.entity.MapSearchInfoEntity
import com.project.navermap.presentation.mainActivity.MainState

sealed class MapLocationSettingState {

    object Uninitialized : MapLocationSettingState()
    object Loading : MapLocationSettingState()

    data class Success(
        val mapSearchInfoEntity: MapSearchInfoEntity,
        val isLocationSame: Boolean,
//        val foodMenuListInBasket: List<RestaurantFoodEntity>? = null
    ) : MapLocationSettingState()

    data class Error(
        @StringRes val errorMessage: Int
    ) : MapLocationSettingState()
}
