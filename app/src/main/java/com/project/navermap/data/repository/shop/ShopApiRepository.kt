package com.project.navermap.data.repository.shop

import com.project.navermap.data.response.shop.ShopInfo

interface ShopApiRepository {
    suspend fun getShopList(
    ): ShopInfo?
}