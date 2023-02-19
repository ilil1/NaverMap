package com.project.navermap.data.repository.address

import com.project.navermap.data.response.kakao.AddressResponse

interface AddressApiRepository {
    suspend fun getAddressInformation(
        address: String
    ): AddressResponse?
}