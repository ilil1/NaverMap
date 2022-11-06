package com.project.navermap.data.repository.address

import com.project.navermap.data.network.KakaoAddressApiService
import com.project.navermap.di.annotation.dispatchermodule.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddressApiRepositoryImpl @Inject constructor(
    private val kakaoAddressApiService: KakaoAddressApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AddressApiRepository {

    override suspend fun getAddressInformation(address: String) =
        withContext(ioDispatcher) {
            val response = kakaoAddressApiService.getAddress(
                address = address
            )
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        }
}