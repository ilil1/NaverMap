package com.project.navermap.data.datasource.restaurant


import com.project.navermap.data.network.FoodApiService
import com.project.navermap.di.annotation.dispatchermodule.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RestaurantDataSourceImpl @Inject constructor(
    private val foodApiService: FoodApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : RestaurantDataSource {

    //flow 를 통해서 entity 를 반환 받는다.
    override suspend fun getItemsByRestaurantId(id: Long) = withContext(ioDispatcher) {
        val response = foodApiService.getRestaurantFoods(id)
        if (response.isSuccessful) {
            response.body()!!
        } else {
            emptyList()
        }
    }
}