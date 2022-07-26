package com.project.navermap.data.repository

import com.project.navermap.ShopInfo

interface ShopApiRepository {
    suspend fun getShopList(
    ): ShopInfo?
}