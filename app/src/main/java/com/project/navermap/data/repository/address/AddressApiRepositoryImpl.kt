package com.project.navermap.data.repository.address

import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.data.network.KakaoAdressApiService
import com.project.navermap.data.network.MapApiService
import com.project.navermap.data.repository.map.MapApiRepository
import com.project.navermap.di.annotation.dispatchermodule.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddressApiRepositoryImpl @Inject constructor(
    private val kakaoAdressApiService: KakaoAdressApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AddressApiRepository {
    override suspend fun getAddressInformation(address: String) =
        withContext(ioDispatcher) {
            val response = kakaoAdressApiService.getAddress(
                address = address
            )
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        }
}