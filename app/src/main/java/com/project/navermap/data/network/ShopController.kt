package com.project.navermap.data.network

import com.project.navermap.ShopInfo
import com.project.navermap.data.url.Url
import retrofit2.Response
import retrofit2.http.GET

interface ShopController {
    @GET(Url.GET_SHOP_LIST)
    suspend fun getList(): Response<ShopInfo>
}
