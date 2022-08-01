package com.project.navermap.screen.myLocation

import androidx.annotation.StringRes
import com.project.navermap.data.entity.AddressHistoryEntity
import com.project.navermap.data.entity.ShopInfoEntity
import com.project.navermap.screen.MainActivity.map.mapFragment.MapState

sealed class MyLocationState {
    object Uninitialized : MyLocationState()
    object Loading : MyLocationState()

    data class Success(
        val addressHistoryList : List<AddressHistoryEntity>?
    ) : MyLocationState()

    data class Error(
        @StringRes val id: Int
    ) : MyLocationState()
}