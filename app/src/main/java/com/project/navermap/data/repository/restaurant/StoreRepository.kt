package com.project.navermap.data.repository.restaurant

import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.data.entity.restaurant.StoreEntity
import com.project.navermap.domain.model.StoreModel
import com.project.navermap.presentation.mainActivity.store.restaurant.StoreCategory

interface StoreRepository {
    suspend fun getList(
        storeCategory: StoreCategory,
        locationLatLngEntity: LocationEntity
    ) : List<StoreEntity>

    suspend fun getItemByStoreId(id : Long) : List<StoreModel>
}