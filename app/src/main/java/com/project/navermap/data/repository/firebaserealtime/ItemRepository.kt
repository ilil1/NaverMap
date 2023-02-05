package com.project.navermap.data.repository.firebaserealtime

import com.project.navermap.data.entity.firebase.ItemEntity
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    fun getItemsByMarketId(marketId: String): Flow<List<ItemEntity>>
}