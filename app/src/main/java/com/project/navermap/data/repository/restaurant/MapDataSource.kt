package com.project.navermap.data.repository.restaurant

import com.project.navermap.data.db.dao.MapSearchCacheDao
import com.project.navermap.data.entity.MapSearchCacheEntity
import com.project.navermap.data.mapper.toMapSearchCacheEntity
import com.project.navermap.data.network.MapApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MapDataSource(
    private val api: MapApiService,
    private val dao: MapSearchCacheDao
) {
    suspend fun getSearchLocationAround(
        categories: String,
        centerLat: String,
        centerLon: String,
        searchType: String = "name",
        radius: String = "1",
        resCoordType: String = "",
        searchtypCd: String = "A",
        reqCoordType: String = "WGS84GEO"
    ): List<MapSearchCacheEntity> {
        val response = api.getSearchLocationAround(
            categories = categories,
            centerLat = centerLat,
            centerLon = centerLon,
            searchType = searchType,
            radius = radius,
            resCoordType = resCoordType,
            searchtypCd = searchtypCd,
            reqCoordType = reqCoordType
        )

        if (response.isSuccessful) {
            // caching result
            val pois = response.body()?.let { _response ->
                _response.searchPoiInfo.pois.poi.map {
                    it.toMapSearchCacheEntity()
                }
            } ?: emptyList()

            dao.clearSearchResultCaches()
            dao.insertSearchResults(pois)
        }

        return dao.getSearchResultCaches()
    }
}