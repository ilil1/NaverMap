package com.project.navermap.data.repository.address

import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.data.response.TmapAddress.AddressInfo
import com.project.navermap.data.response.kakao.AddressResponse

interface AddressApiRepository {
    suspend fun getAddressInformation(
        address: String
    ): AddressResponse?
}