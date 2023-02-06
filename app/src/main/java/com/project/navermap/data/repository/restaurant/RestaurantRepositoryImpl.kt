package com.project.navermap.data.repository.restaurant

import com.project.navermap.data.datasource.restaurant.RestaurantDataSource
import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.data.mapper.toRestaurantEntity
import com.project.navermap.data.mapper.toRestaurantModel
import com.project.navermap.data.network.FoodApiService
import com.project.navermap.di.annotation.dispatchermodule.IoDispatcher
import com.project.navermap.domain.model.RestaurantModel
import com.project.navermap.presentation.mainActivity.store.restaurant.RestaurantCategory
import com.project.navermap.util.provider.ResourcesProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

//repository 에서 Entity 를 Model 로 변환해서 가져온다.
class RestaurantRepositoryImpl @Inject constructor(
    private val restaurantDataSource : RestaurantDataSource,
    private val mapDataSource: MapDataSource,
    private val resourcesProvider: ResourcesProvider
) : RestaurantRepository {

    //entity 를 반환 받은 다음에 여기서는 Model 모 return 만 해준다.
    override suspend fun getList(
        restaurantCategory: RestaurantCategory,
        locationLatLngEntity: LocationEntity
    ): List<RestaurantModel> {
        return mapDataSource.getSearchLocationAround(
            categories = resourcesProvider.getString(restaurantCategory.categoryTypeId),
            centerLat = locationLatLngEntity.latitude.toString(),
            centerLon = locationLatLngEntity.longitude.toString()
        ).map {
            it.toRestaurantEntity(restaurantCategory).toRestaurantModel()
        }
    }

    //플로우에 맞게 수정 필요함
    override suspend fun getItemsByRestaurantId(id: Long) =
        restaurantDataSource.getItemsByRestaurantId(id).map { it ->
            it.map {
                return@map it.toModel(id)
            }
        }
}