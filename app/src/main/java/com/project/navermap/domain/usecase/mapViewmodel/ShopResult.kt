package com.project.navermap.domain.usecase.mapViewmodel

sealed class ShopResult {
    object Success : ShopResult()
    object Failure : ShopResult()
}



