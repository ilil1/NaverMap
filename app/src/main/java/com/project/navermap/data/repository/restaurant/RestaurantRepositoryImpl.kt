package com.project.navermap.data.repository.restaurant

import android.util.Log
import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.data.entity.restaurant.RestaurantEntity
import com.project.navermap.data.network.FoodApiService
import com.project.navermap.data.network.MapApiService
import com.project.navermap.di.annotation.dispatchermodule.IoDispatcher
import com.project.navermap.presentation.mainActivity.store.restaurant.RestaurantCategory
import com.project.navermap.util.provider.ResourcesProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RestaurantRepositoryImpl @Inject constructor(
    private val mapApiService: MapApiService,
    private val foodApiService: FoodApiService,
    private val resourcesProvider: ResourcesProvider,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : RestaurantRepository {

    override suspend fun getList(
        restaurantCategory: RestaurantCategory,
        locationLatLngEntity: LocationEntity
    ): List<RestaurantEntity> = withContext(ioDispatcher) {

        Log.d("getList", resourcesProvider.getString(restaurantCategory.categoryTypeId))

        val response = mapApiService.getSearchLocationAround(
            categories = resourcesProvider.getString(restaurantCategory.categoryTypeId),
            centerLat = locationLatLngEntity.latitude.toString(),
            centerLon = locationLatLngEntity.longitude.toString(),
            searchType = "name",
            radius = "1",
            resCoordType = "",
            searchtypCd = "A",
            reqCoordType = "WGS84GEO"
        )

        if (response.isSuccessful) {
            response.body()?.searchPoiInfo?.pois?.poi?.mapIndexed { index, poi ->
                RestaurantEntity(
                    id = hashCode().toLong(),
                    restaurantInfoId = (1..10).random().toLong(),
                    restaurantCategory = restaurantCategory,
                    restaurantTitle = poi.name ?: "제목 없음",
                    restaurantImageUrl = "https://picsum.photos/200",
                    grade = (1 until 5).random() + ((0..10).random() / 10f),
                    reviewCount = (0 until 200).random(),
                    deliveryTimeRange = Pair((0..20).random(), (40..60).random()),
                    deliveryTipRange = Pair((0..1000).random(), (2000..4000).random()),
                    restaurantTelNumber = poi.telNo,
                    latitude = poi.frontLat.toDouble(),
                    longitude = poi.frontLon.toDouble(),
                    isMarketOpen = true,
                    distance = (0 until 3).random() + ((0..10).random() / 10f)
                )
            } ?: listOf()
        } else {
            listOf()
        }
    }

    override suspend fun getItemsByRestaurantId(id: Long) = withContext(ioDispatcher) {
        val response = foodApiService.getRestaurantFoods(id)
        if (response.isSuccessful) {
            response.body()!!.map {
                it.toModel(id)
            }
        } else {
            emptyList()
        }
    }
}