package com.project.navermap.presentation.MainActivity.myinfo.like

import androidx.annotation.StringRes
import com.project.navermap.domain.model.Model

sealed class LikeState {
    object Uninitialized : LikeState()
    object Loading : LikeState()

    data class Success<T : Model>(
        val modelList: List<T>
    ) : LikeState()

    data class Error(
        @StringRes val resId: Int
    ) : LikeState()
}