package com.project.navermap.data.datasource.mapData

import android.accounts.NetworkErrorException
import com.project.navermap.data.db.dao.MapSearchCacheDao
import com.project.navermap.data.entity.MapSearchCacheEntity
import com.project.navermap.data.mapper.toMapSearchCacheEntity
import com.project.navermap.data.network.MapApiService
import com.project.navermap.data.response.restaurant.RestaurantFoodDto
import com.project.navermap.di.annotation.dispatchermodule.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MapDataSource(
    private val api: MapApiService,
    private val dao: MapSearchCacheDao,
    private val ioDispatcher: CoroutineDispatcher
) {
    //코루틴
//    suspend fun getSearchLocationAround(
//        categories: String,
//        centerLat: String,
//        centerLon: String,
//        searchType: String = "name",
//        radius: String = "1",
//        resCoordType: String = "",
//        searchtypCd: String = "A",
//        reqCoordType: String = "WGS84GEO"
//    ): List<MapSearchCacheEntity> {
//        val response = api.getSearchLocationAround(
//            categories = categories,
//            centerLat = centerLat,
//            centerLon = centerLon,
//            searchType = searchType,
//            radius = radius,
//            resCoordType = resCoordType,
//            searchtypCd = searchtypCd,
//            reqCoordType = reqCoordType
//        )
//
//        if (response.isSuccessful) {
//            // caching result
//            val pois = response.body()?.let { _response ->
//                _response.searchPoiInfo.pois.poi.map {
//                    it.toMapSearchCacheEntity()
//                }
//            } ?: emptyList()
//
//            dao.clearSearchResultCaches()
//            dao.insertSearchResults(pois)
//        }
//
//        return dao.getSearchResultCaches()
//    }

    //플로우
    suspend fun getSearchLocationAround(
        categories: String,
        centerLat: String,
        centerLon: String,
        searchType: String = "name",
        radius: String = "1",
        resCoordType: String = "",
        searchtypCd: String = "A",
        reqCoordType: String = "WGS84GEO"
    ): Flow<List<MapSearchCacheEntity>> =

        flow {
            runCatching {
                api.getSearchLocationAround(
                    categories = categories,
                    centerLat = centerLat,
                    centerLon = centerLon,
                    searchType = searchType,
                    radius = radius,
                    resCoordType = resCoordType,
                    searchtypCd = searchtypCd,
                    reqCoordType = reqCoordType
                )
            }.onSuccess { response ->

                if (response.isSuccessful) {
                    // caching result
                    val pois = response.body()?.let { _response ->
                        _response.searchPoiInfo.pois.poi.map {
                            it.toMapSearchCacheEntity()
                        }
                    } ?: emptyList()

                    dao.clearSearchResultCaches()
                    dao.insertSearchResults(pois)
                    emit(dao.getSearchResultCaches())

                } else {
                    val errorBody = response.errorBody()?.string()
                    throw NetworkErrorException(errorBody)
                }
            }.onFailure {
                throw it
            }
        }.flowOn(ioDispatcher)

}