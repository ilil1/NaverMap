package com.project.navermap.data.network

import com.project.navermap.data.response.kakao.AddressResponse
import com.project.navermap.data.url.Key
import com.project.navermap.data.url.Url
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoAddressApiService {
    @GET(Url.KakaoAdress_URL)
    suspend fun getAddress(
        @Header("Authorization") key: String = Key.GEOCODE_USER_INFO,
        @Query("query") address: String
    ): Response<AddressResponse>
}