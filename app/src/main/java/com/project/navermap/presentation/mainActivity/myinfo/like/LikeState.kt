package com.project.navermap.presentation.mainActivity.myinfo.like

import androidx.annotation.StringRes
import com.project.navermap.data.entity.ShopInfoEntity
import com.project.navermap.domain.model.Model
import com.project.navermap.domain.usecase.mapViewmodel.ShopResult

sealed class LikeState {
    object Uninitialized : LikeState()
    object Loading : LikeState()

    data class Success(
        val modelList : ShopInfoEntity?
    ) : LikeState()

    data class Error(
        @StringRes val resId: Int
    ) : LikeState()
}