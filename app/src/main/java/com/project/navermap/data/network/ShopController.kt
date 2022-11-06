package com.project.navermap.data.network

import com.project.navermap.data.response.shop.ShopInfo
import com.project.navermap.data.url.Url
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ShopController {
    @GET(Url.GET_SHOP_LIST)
    suspend fun getList(): Response<ShopInfo>


    @GET(Url.GET_SHOP_LIST)
    suspend fun getStoreList(
        @Path("storeId") storeId : Long
    ) : Response<List<ShopInfo>>
}
