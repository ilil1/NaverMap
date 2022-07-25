package com.project.navermap.data.repository

import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.data.network.MapApiService
import com.project.navermap.di.annotation.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext
import javax.inject.Inject

//Inject?
class MapApiRepositoryImpl @Inject constructor(
    private val mapApiService: MapApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : MapApiRepository {
    override suspend fun getReverseGeoInformation(locationLatLngEntity: LocationEntity) =
        withContext(ioDispatcher) {
            val response = mapApiService.getReverseGeoCode(
                lat = locationLatLngEntity.latitude,
                lon = locationLatLngEntity.longitude
            )
            if (response.isSuccessful) {
                response.body()?.addressInfo
            } else {
                null
            }
        }
}