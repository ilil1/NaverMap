package com.project.navermap.data.repository.home

import com.project.navermap.domain.model.HomeItemModel
import com.project.navermap.domain.model.TownMarketModel
import com.project.navermap.domain.model.category.HomeListCategory


interface HomeRepository {

    /** 모든 동네마켓의 리스트를 불러오는 메소드 */
    fun getAllMarketList(): List<TownMarketModel>

    /** HomeListCategory에 따라서 분기하여 해당하는 아이템들을 모두 호출하는 메서드 */
    fun findItemsByCategory(homeListCategory: HomeListCategory): List<HomeItemModel>

    /**새로 등록된 모든 할인 상품을 가져오는 Method*/
    fun getAllNewSaleItems(): List<HomeItemModel>
}