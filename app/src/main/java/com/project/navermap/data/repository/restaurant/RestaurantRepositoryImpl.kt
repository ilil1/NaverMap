package com.project.navermap.data.repository.restaurant

import android.content.ContentValues
import android.util.Log
import com.project.navermap.data.datasource.restaurant.RestaurantDataSource
import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.data.mapper.toRestaurantEntity
import com.project.navermap.data.mapper.toRestaurantModel
import com.project.navermap.domain.model.FoodModel
import com.project.navermap.domain.model.RestaurantModel
import com.project.navermap.presentation.mainActivity.store.restaurant.RestaurantCategory
import com.project.navermap.util.provider.ResourcesProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    //코루틴 ver
//    override suspend fun getItemsByRestaurantId(id: Long) =
//        restaurantDataSource.getItemsByRestaurantId(id).map {
//            it.toModel(id)
//        }

    //플로우 ver
    override suspend fun getItemsByRestaurantId(id: Long) : Flow<List<FoodModel>> =

        restaurantDataSource.getItemsByRestaurantId(id).map {
            it.map {
                return@map it.toModel(id)
            }
        }
}