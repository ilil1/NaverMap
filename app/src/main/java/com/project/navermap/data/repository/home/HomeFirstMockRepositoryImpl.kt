package com.project.navermap.data.repository.home

import com.project.navermap.domain.model.CellType
import com.project.navermap.domain.model.FoodModel
import com.project.navermap.domain.model.RestaurantModel
import com.project.navermap.presentation.mainActivity.store.restaurant.RestaurantCategory
import javax.inject.Inject

class HomeFirstMockRepositoryImpl @Inject constructor(

) : HomeFirstMockRepository {

//    id = 4,
//    type = CellType.HOME_DETAIL_ITEM_CELL,
//    restaurantTitle = "동산동 노래레슨",
//    distance = 1.4f,
//    deliveryTimeRange = Pair(10,15),
//    grade = 32f,
//    reviewCount = 12,
//    deliveryTipRange = Pair(2000,5000),
//    latitude = 2.4,
//    longitude = 3.6,
//    isMarketOpen = true,
//    restaurantInfoId = 0,
//    restaurantCategory = RestaurantCategory.ALL,
//    restaurantImageUrl = "",
//    restaurantTelNumber = "0"

    override fun getAllData(): List<RestaurantModel> {
        val mockList = listOf(
            RestaurantModel(
                id = 0,
                type = CellType.HOME_DETAIL_ITEM_CELL,
                restaurantTitle = "동성로 필터",
                distance = 1.4f,
                deliveryTimeRange = Pair(10,15),
                grade = 32f,
                reviewCount = 12,
                deliveryTipRange = Pair(2000,5000),
                latitude = 2.4,
                longitude = 3.6,
                isMarketOpen = true,
                restaurantInfoId = 0,
                restaurantCategory = RestaurantCategory.ALL,
                restaurantImageUrl = "",
                restaurantTelNumber = "0"
            ),
            RestaurantModel(
                id = 1,
                type = CellType.HOME_DETAIL_ITEM_CELL,
                restaurantTitle = "중구 필터",
                distance = 1.4f,
                deliveryTimeRange = Pair(10,15),
                grade = 32f,
                reviewCount = 12,
                deliveryTipRange = Pair(2000,5000),
                latitude = 2.4,
                longitude = 3.6,
                isMarketOpen = true,
                restaurantInfoId = 0,
                restaurantCategory = RestaurantCategory.ALL,
                restaurantImageUrl = "",
                restaurantTelNumber = "0"
            ),
            RestaurantModel(
                id = 2,
                type = CellType.HOME_DETAIL_ITEM_CELL,
                restaurantTitle = "영남대 필터",
                distance = 1.7f,
                deliveryTimeRange = Pair(10,15),
                grade = 1f,
                reviewCount = 2,
                deliveryTipRange = Pair(2000,5000),
                latitude = 2.4,
                longitude = 3.6,
                isMarketOpen = true,
                restaurantInfoId = 0,
                restaurantCategory = RestaurantCategory.ALL,
                restaurantImageUrl = "",
                restaurantTelNumber = "0"
            ),
            RestaurantModel(
                id = 3,
                type = CellType.HOME_DETAIL_ITEM_CELL,
                restaurantTitle = "경산 필터",
                distance = 1.4f,
                deliveryTimeRange = Pair(10,15),
                grade = 32f,
                reviewCount = 12,
                deliveryTipRange = Pair(2000,5000),
                latitude = 2.4,
                longitude = 3.6,
                isMarketOpen = true,
                restaurantInfoId = 0,
                restaurantCategory = RestaurantCategory.ALL,
                restaurantImageUrl = "",
                restaurantTelNumber = "0"
            ),
            RestaurantModel(
                id = 4,
                type = CellType.HOME_DETAIL_ITEM_CELL,
                restaurantTitle = "복현동 필터",
                distance = 1.4f,
                deliveryTimeRange = Pair(10,15),
                grade = 32f,
                reviewCount = 12,
                deliveryTipRange = Pair(2000,5000),
                latitude = 2.4,
                longitude = 3.6,
                isMarketOpen = true,
                restaurantInfoId = 0,
                restaurantCategory = RestaurantCategory.ALL,
                restaurantImageUrl = "",
                restaurantTelNumber = "0"
            ),
            RestaurantModel(
                id = 5,
                type = CellType.HOME_DETAIL_ITEM_CELL,
                restaurantTitle = "복현동 필터",
                distance = 1.4f,
                deliveryTimeRange = Pair(10,15),
                grade = 32f,
                reviewCount = 12,
                deliveryTipRange = Pair(2000,5000),
                latitude = 2.4,
                longitude = 3.6,
                isMarketOpen = true,
                restaurantInfoId = 0,
                restaurantCategory = RestaurantCategory.ALL,
                restaurantImageUrl = "",
                restaurantTelNumber = "0"
            )

        )
        return mockList
    }
}