package com.project.navermap.presentation.mainActivity.home.suggestItemDetail.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.navermap.data.entity.firebase.ReviewEntity
import com.project.navermap.data.repository.firebaserealtime.ReviewRepository
import com.project.navermap.domain.model.ReviewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import java.util.concurrent.Flow
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    lateinit var marketId: String

    private val _reviewData: LiveData<List<ReviewModel>> = reviewRepository.getReviewData(marketId)
        .map { list -> list.map { reviewEntity -> reviewEntity.toModel() } }.asLiveData()

}
