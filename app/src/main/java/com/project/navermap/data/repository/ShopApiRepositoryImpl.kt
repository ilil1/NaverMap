package com.project.navermap.data.repository

import com.project.navermap.data.network.ShopController
import com.project.navermap.di.annotation.dispatchermodule.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopApiRepositoryImpl @Inject constructor(
    private val shopController: ShopController,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ShopApiRepository {
    override suspend fun getShopList() =
        withContext(ioDispatcher) {
            val response = shopController.getList()
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        }
}