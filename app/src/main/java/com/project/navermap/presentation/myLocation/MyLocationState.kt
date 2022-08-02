package com.project.navermap.presentation.myLocation

import androidx.annotation.StringRes
import com.project.navermap.data.entity.AddressHistoryEntity

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