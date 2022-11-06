package com.project.navermap.data.repository.restaurant

import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.data.entity.restaurant.RestaurantEntity
import com.project.navermap.data.entity.restaurant.StoreEntity
import com.project.navermap.data.network.MapApiService
import com.project.navermap.data.network.ShopController
import com.project.navermap.data.repository.map.MapApiRepository
import com.project.navermap.di.annotation.dispatchermodule.IoDispatcher
import com.project.navermap.domain.model.StoreModel
import com.project.navermap.presentation.mainActivity.store.restaurant.StoreCategory
import com.project.navermap.util.provider.ResourcesProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val mapApiService: MapApiService,
    private val shopController: ShopController,
    private val resourcesProvider: ResourcesProvider,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : StoreRepository {

    override suspend fun getList(
        storeCategory: StoreCategory,
        locationLatLngEntity: LocationEntity
    ): List<StoreEntity> = withContext(ioDispatcher) {

        val response = mapApiService.getSearchLocationAround(
            categories = resourcesProvider.getString(storeCategory.storeCategoryNameId),
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
                StoreEntity(
                    id = hashCode().toLong(),
                    storeInfoId = (1..10).random().toLong(),
                    storeCategory = storeCategory,
                    storeTitle = poi.name ?: "제목 없음",
                    storeImageUrl = "https://picsum.photos/200",
                    grade = (1 until 5).random() + ((0..10).random() / 10f),
                    reviewCount = (0 until 200).random(),
                    deliveryTimeRange = Pair((0..20).random(), (40..60).random()),
                    deliveryTipRange = Pair((0..1000).random(), (2000..4000).random()),
                    restaurantTelNumber = poi.telNo,
                    latitude = poi.frontLat.toDouble(),
                    longitude = poi.frontLon.toDouble()
                )
            } ?: listOf()
        } else {
            listOf()
        }
    }

    override suspend fun getItemByStoreId(id: Long): List<StoreModel> {
        TODO("Not yet implemented")
    }


}