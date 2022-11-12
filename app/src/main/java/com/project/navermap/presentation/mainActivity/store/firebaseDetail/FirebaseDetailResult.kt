package com.project.navermap.presentation.mainActivity.store.firebaseDetail

import com.project.navermap.data.entity.firebase.FirebaseEntity
import com.project.navermap.data.entity.restaurant.RestaurantEntity
import com.project.navermap.presentation.mainActivity.store.storeDetail.StoreDetailResult

sealed class FirebaseDetailResult {
    object Uninitialized: FirebaseDetailResult()

    object Loading : FirebaseDetailResult()

    data class Success(
        val firebaseEntity: FirebaseEntity
    ): FirebaseDetailResult()
}