package com.project.navermap.data.repository.firebaserealtime

import com.project.navermap.data.entity.firebase.ReviewEntity
import kotlinx.coroutines.flow.Flow

interface ReviewRepository {

    fun getReviewData(marketid : String) : Flow<List<ReviewEntity>>
}