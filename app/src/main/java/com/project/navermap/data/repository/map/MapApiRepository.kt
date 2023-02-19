package com.project.navermap.data.repository.map

import com.project.navermap.data.response.tmapAddress.AddressInfo
import com.project.navermap.data.entity.LocationEntity

interface MapApiRepository {
    suspend fun getReverseGeoInformation(
        locationLatLngEntity: LocationEntity
    ): AddressInfo?
}