package com.project.navermap.presentation.mainActivity.home.suggestItemDetail

import androidx.lifecycle.*
import com.project.navermap.data.entity.SuggestItemEntity
import com.project.navermap.data.repository.home.HomeFirstMockRepository
import com.project.navermap.domain.model.RestaurantModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SuggestItemDetailViewModel @AssistedInject constructor(
    @Assisted private val suggestItemEntity: SuggestItemEntity,
    private val firstMockRepository: HomeFirstMockRepository
): ViewModel() {

    /**
     * Hilt 런타임 주입
     */
    @AssistedFactory
    interface SuggestItemDetailAssistedFactory {
        fun create(suggestItemEntity: SuggestItemEntity
        ): SuggestItemDetailViewModel
    }


    private val _homeListData = MutableLiveData<List<RestaurantModel>>()
    val homeListData : LiveData<List<RestaurantModel>> = _homeListData

    fun firstFetchData() : Job = viewModelScope.launch {
        _homeListData.value = firstMockRepository.getAllData()
    }

    companion object {
        fun provideFactory(
            assistedFactory: SuggestItemDetailAssistedFactory,
            suggestItemEntity: SuggestItemEntity
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(suggestItemEntity) as T
            }
        }
    }

    private val _storeDetailResultLiveData = MutableLiveData<SuggestItemDetailResult>(SuggestItemDetailResult.Uninitialized)
    val storeDetailResultLiveData: LiveData<SuggestItemDetailResult> get() = _storeDetailResultLiveData

    fun fetchData(): Job = viewModelScope.launch {
        _storeDetailResultLiveData.value = SuggestItemDetailResult.Loading
        _storeDetailResultLiveData.value = SuggestItemDetailResult.Success(
            suggestItemEntity = suggestItemEntity
        )
    }

}