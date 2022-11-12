package com.project.navermap.data.repository.home

import com.project.navermap.domain.model.CellType
import com.project.navermap.domain.model.RestaurantModel
import com.project.navermap.presentation.mainActivity.store.restaurant.RestaurantCategory
import javax.inject.Inject

class HomeSecondMockRepositoryImpl @Inject constructor(

//    override val id: Long,
//    override val type: CellType = CellType.HOME_TOWN_MARKET_CELL,
//    val restaurantInfoId: Long,
//    val restaurantCategory: RestaurantCategory,
//    val restaurantTitle: String,
//    val restaurantImageUrl: String,
//    val grade: Float,
//    val reviewCount: Int,
//    val deliveryTimeRange: Pair<Int, Int>,
//    val deliveryTipRange: Pair<Int, Int>,
//    val restaurantTelNumber: String?,
//    var latitude: Double = 0.0,
//    var longitude: Double = 0.0,
//    val isMarketOpen: Boolean,
//    val distance: Float


) : HomeSecondMockRepository {
    override fun getAllData(): List<RestaurantModel> {
        val mockList = listOf(
            RestaurantModel(
                id = 0,
                type = CellType.HOME_DETAIL_ITEM_CELL,
                restaurantTitle = "동성로 가왕레슨",
                distance = 1.4f,
                deliveryTimeRange = Pair(10,15),
                grade = 2.5f,
                reviewCount = 10,
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
                restaurantTitle = "중구 가요교실",
                distance = 1.4f,
                deliveryTimeRange = Pair(12,15),
                grade = 2.5f,
                reviewCount = 10,
                deliveryTipRange = Pair(3000,5000),
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
                restaurantTitle = "벙커 스튜디오 노래 레슨",
                distance = 1.6f,
                deliveryTimeRange = Pair(10,15),
                grade = 2.3f,
                reviewCount = 1,
                deliveryTipRange = Pair(2000,5000),
                latitude = 5.6,
                longitude = 7.6,
                isMarketOpen = true,
                restaurantInfoId = 0,
                restaurantCategory = RestaurantCategory.ALL,
                restaurantImageUrl = "",
                restaurantTelNumber = "0"
            ),
            RestaurantModel(
                id = 3,
                type = CellType.HOME_DETAIL_ITEM_CELL,
                restaurantTitle = "동구 스튜디오",
                distance = 1.2f,
                deliveryTimeRange = Pair(10,15),
                grade = 10f,
                reviewCount = 3,
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
                restaurantTitle = "동산동 노래레슨",
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
                restaurantTitle = "가요레슨",
                distance = 1.4f,
                deliveryTimeRange = Pair(10,15),
                grade = 2.5f,
                reviewCount = 0,
                deliveryTipRange = Pair(2000,5000),
                latitude = 2.4,
                longitude = 3.6,
                isMarketOpen = true,
                restaurantInfoId = 0,
                restaurantCategory = RestaurantCategory.ALL,
                restaurantImageUrl = "",
                restaurantTelNumber = "0"
            ),

        )
        return mockList
    }
}