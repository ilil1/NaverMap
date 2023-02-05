package com.project.navermap.presentation.mainActivity.store

import androidx.lifecycle.*
import com.project.navermap.data.entity.firebase.ReviewEntity
import com.project.navermap.data.repository.firebaserealtime.ReviewRepository
import com.project.navermap.domain.model.ReviewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _reviewData = MutableLiveData<List<ReviewEntity>>(emptyList())
    val reviewData: LiveData<List<ReviewModel>> get() = _reviewData
        .map { list -> list.map { reviewEntity -> reviewEntity.toModel() } }

    fun getReviewByMarketId(marketId: String) = reviewRepository.getReviewData(marketId).onEach {
        _reviewData.value = it
    }.launchIn(viewModelScope)

    fun write(marketId: String,title : String, content : String, rating :Int) = viewModelScope.launch{
        reviewRepository.writeReviewData(marketId, title, content, rating)
    }

}
