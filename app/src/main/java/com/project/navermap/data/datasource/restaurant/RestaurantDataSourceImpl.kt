package com.project.navermap.data.datasource.restaurant


import android.accounts.NetworkErrorException
import com.project.navermap.data.network.FoodApiService
import com.project.navermap.data.response.restaurant.RestaurantFoodDto
import com.project.navermap.di.annotation.dispatchermodule.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RestaurantDataSourceImpl @Inject constructor(
    private val foodApiService: FoodApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : RestaurantDataSource {

// 코루틴
//    override suspend fun getItemsByRestaurantId(id: Long) = withContext(ioDispatcher) {
//        val response = foodApiService.getRestaurantFoods(id)
//        if (response.isSuccessful) {
//            response.body()!!
//        } else {
//            emptyList()
//        }
//    }

    //플로우
    override suspend fun getItemsByRestaurantId(id: Long): Flow<List<RestaurantFoodDto>> =
        flow {
            runCatching {
                foodApiService.getRestaurantFoods(id)
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    emit(response.body()!!)
                } else {
                    val errorBody = response.errorBody()?.string()
                    throw NetworkErrorException(errorBody)
                }
            }.onFailure {
                throw it
            }
        }.flowOn(ioDispatcher)
}