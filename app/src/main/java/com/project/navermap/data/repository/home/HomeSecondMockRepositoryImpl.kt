package com.project.navermap.data.repository.home

import com.project.navermap.domain.model.CellType
import com.project.navermap.domain.model.FoodModel
import javax.inject.Inject

class HomeSecondMockRepositoryImpl @Inject constructor(

 //    override val id: Long,
//    override val type: CellType = CellType.FOOD_CELL,
//    val title: String,
//    val description: String,
//    val price: String,
//    val imageUrl: String,
//    val restaurantId: Long


) : HomeSecondMockRepository {
    override fun getAllData(): List<FoodModel> {
        val mockList = listOf(
            FoodModel(
                id = 0,
                type = CellType.HOME_DETAIL_ITEM_CELL,
                title = "동성로 가왕레슨",
                description = "노래레슨업체",
                price = "140000",
                imageUrl = "",
                restaurantId = 0
            ),
            FoodModel(
                id = 1,
                type = CellType.HOME_DETAIL_ITEM_CELL,
                title = "중구 가요교실",
                description = "노래레슨업체",
                price = "120000",
                imageUrl = "",
                restaurantId = 1
            ),
            FoodModel(
                id = 2,
                type = CellType.HOME_DETAIL_ITEM_CELL,
                title = "벙커 스튜디오 노래 레슨",
                description = "노래레슨업체",
                price = "160000",
                imageUrl = "",
                restaurantId = 2
            ),
            FoodModel(
                id = 3,
                type = CellType.HOME_DETAIL_ITEM_CELL,
                title = "동구 스튜디오",
                description = "노래레슨업체",
                price = "135000",
                imageUrl = "",
                restaurantId = 3
            ),
            FoodModel(
                id = 4,
                type = CellType.HOME_DETAIL_ITEM_CELL,
                title = "동산동 노래레슨",
                description = "노래레슨업체",
                price = "155000",
                imageUrl = "",
                restaurantId = 4
            ),
            FoodModel(
                id = 5,
                type = CellType.HOME_DETAIL_ITEM_CELL,
                title = "가요레슨",
                description = "노래레슨업체",
                price = "125000",
                imageUrl = "",
                restaurantId = 5
            ),

        )
        return mockList
    }
}