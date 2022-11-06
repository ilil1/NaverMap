package com.project.navermap.domain.usecase.storeListViewModel

import com.project.navermap.domain.model.StoreModel

sealed class StoreResult {
    data class Sucess(val data : List<StoreModel>) : StoreResult()
    object Failure : StoreResult()
}