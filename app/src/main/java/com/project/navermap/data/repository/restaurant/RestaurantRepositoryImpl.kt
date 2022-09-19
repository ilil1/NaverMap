package com.project.navermap.data.repository.restaurant

import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.data.mapper.toRestaurantEntity
import com.project.navermap.data.mapper.toRestaurantModel
import com.project.navermap.data.network.FoodApiService
import com.project.navermap.di.annotation.dispatchermodule.IoDispatcher
import com.project.navermap.domain.model.RestaurantModel
import com.project.navermap.presentation.mainActivity.store.restaurant.RestaurantCategory
import com.project.navermap.util.provider.ResourcesProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RestaurantRepositoryImpl @Inject constructor(
    private val mapDataSource: MapDataSource,
    private val foodApiService: FoodApiService,
    private val resourcesProvider: ResourcesProvider,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : RestaurantRepository {

    override fun getList(
        restaurantCategory: RestaurantCategory,
        locationLatLngEntity: LocationEntity
    ): Flow<List<RestaurantModel>> {
        return mapDataSource.getSearchLocationAround(
            categories = resourcesProvider.getString(restaurantCategory.categoryTypeId),
            centerLat = locationLatLngEntity.latitude.toString(),
            centerLon = locationLatLngEntity.longitude.toString()
        ).map {
            it.map { entity ->
                entity.toRestaurantEntity(restaurantCategory).toRestaurantModel()
            }
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