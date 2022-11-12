package com.project.navermap.data.repository.home

import com.project.navermap.domain.model.CellType
import com.project.navermap.domain.model.FoodModel
import javax.inject.Inject

class HomeFirstMockRepositoryImpl @Inject constructor(

) : HomeFirstMockRepository {

    override fun getAllData(): List<FoodModel> {
        val mockList = listOf(
            FoodModel(
                id = 0,
                type = CellType.HOME_DETAIL_ITEM_CELL,
                title = "동성로 필터",
                description = "필터청소업체",
                price = "40000 ~ 70000",
                imageUrl = "",
                restaurantId = 0
            ),
            FoodModel(
                id = 1,
                type = CellType.HOME_DETAIL_ITEM_CELL,
                title = "중구 필터",
                description = "필터청소업체",
                price = "20000 ~ 50000",
                imageUrl = "",
                restaurantId = 1
            ),
            FoodModel(
                id = 2,
                type = CellType.HOME_DETAIL_ITEM_CELL,
                title = "영남대 필터",
                description = "필터청소업체",
                price = "30000 ~ 50000",
                imageUrl = "",
                restaurantId = 2
            ),
            FoodModel(
                id = 3,
                type = CellType.HOME_DETAIL_ITEM_CELL,
                title = "경산 필터",
                description = "필터청소업체",
                price = "40000 ~ 59000",
                imageUrl = "",
                restaurantId = 3
            ),
            FoodModel(
                id = 4,
                type = CellType.HOME_DETAIL_ITEM_CELL,
                title = "복현동 필터",
                description = "필터청소업체",
                price = "60000 ~ 90000",
                imageUrl = "",
                restaurantId = 4
            ),
            FoodModel(
                id = 5,
                type = CellType.HOME_DETAIL_ITEM_CELL,
                title = "복현동 필터",
                description = "필터청소업체",
                price = "55000 ~ 65000",
                imageUrl = "",
                restaurantId = 5
            )

        )
        return mockList
    }
}