package com.project.navermap.domain.usecase.mainViewmodel

import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.data.repository.map.MapApiRepository

class GetReverseGeoUseCase(
    private val repository: MapApiRepository
) {
    suspend operator fun invoke(location: LocationEntity) =
        repository.getReverseGeoInformation(location)
}