package com.project.navermap.data.repository.Like

import com.project.navermap.data.db.LikeDB
import com.project.navermap.data.entity.LikeMarketEntity
import com.project.navermap.di.annotation.dispatchermodule.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LikeRepository @Inject constructor(
    private val likeDb : LikeDB,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
){

    suspend fun getAllLikeData() = withContext(ioDispatcher){
        likeDb.likeDataDao().getAllLikeData()
    }

    suspend fun insertLikeData(marketEntity: LikeMarketEntity) = withContext(ioDispatcher){
        likeDb.likeDataDao().insertData(marketEntity)
    }

    suspend fun deleteAllLikeData(){
        likeDb.likeDataDao().deleteAllLike()
    }

}