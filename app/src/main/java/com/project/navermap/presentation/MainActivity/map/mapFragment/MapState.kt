package com.project.navermap.presentation.MainActivity.map.mapFragment

import androidx.annotation.StringRes
import com.project.navermap.data.entity.ShopInfoEntity


sealed class MapState {
    object Uninitialized : MapState()
    object Loading : MapState()

    data class Success(
        val shopInfoList: List<ShopInfoEntity>?
    ) : MapState()

    data class Error(
        @StringRes val id: Int
    ) : MapState()
}
