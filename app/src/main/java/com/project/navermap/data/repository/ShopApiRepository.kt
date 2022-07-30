package com.project.navermap.data.repository

import com.project.navermap.data.response.shop.ShopInfo

interface ShopApiRepository {
    suspend fun getShopList(
    ): ShopInfo?
}