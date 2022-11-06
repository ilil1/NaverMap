package com.project.navermap.presentation.mainActivity.store.storeDetail.menu

import androidx.annotation.StringRes
import com.project.navermap.domain.model.Model

sealed class StoreMenuState {
//나중에 사용할 클래스
    object Uninitialized : StoreMenuState()

    object Loading : StoreMenuState()

    object ListLoaded : StoreMenuState()

    data class Success<T : Model>(
        val saleList : List<T>,
    ) : StoreMenuState()

    data class Error(
        @StringRes val errorMessage: Int
    ) : StoreMenuState()
}