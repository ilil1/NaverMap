package com.project.navermap

import com.project.navermap.ShopInfo
import com.project.navermap.Url
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ShopController {
    @GET(Url.GET_SHOP_LIST)
    suspend fun getList(): Response<ShopInfo>
}
