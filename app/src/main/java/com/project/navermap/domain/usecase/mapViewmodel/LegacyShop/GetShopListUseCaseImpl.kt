package com.project.navermap.domain.usecase.mapViewmodel.LegacyShop

import com.project.navermap.data.entity.ShopInfoEntity
import com.project.navermap.data.repository.shop.ShopApiRepository
import com.project.navermap.domain.usecase.mapViewmodel.ShopResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetShopListUseCaseImpl(
    private val shopApiRepositoryImpl: ShopApiRepository,
    private val ioDispatcher: CoroutineDispatcher
) {
    private var shopList: MutableList<ShopInfoEntity> = mutableListOf()

    //레거시
    suspend fun getApiShopList() = withContext(ioDispatcher) {
        val list = shopApiRepositoryImpl.getShopList()?.shopList
        list?.let { shopInfoResult ->
            shopInfoResult.forEach { shopInfoResult ->
                shopList.add(
                    ShopInfoEntity(
                        shop_id = shopInfoResult.shop_id,
                        shop_name = shopInfoResult.shop_name,
                        is_open = shopInfoResult.is_open,
                        lot_number_address = shopInfoResult.lot_number_address,
                        road_name_address = shopInfoResult.road_name_address,
                        latitude = shopInfoResult.latitude,
                        longitude = shopInfoResult.longitude,
                        average_score = shopInfoResult.average_score,
                        review_number = shopInfoResult.review_number,
                        main_image = shopInfoResult.main_image,
                        description = shopInfoResult.description,
                        category = shopInfoResult.category,
                        detail_category = shopInfoResult.detail_category,
                        is_branch = shopInfoResult.is_branch,
                        branch_name = shopInfoResult.branch_name
                    )
                )
            }
            ShopResult.Success
        }
    }
}