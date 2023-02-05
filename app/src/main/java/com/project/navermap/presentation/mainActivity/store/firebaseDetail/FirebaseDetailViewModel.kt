package com.project.navermap.presentation.mainActivity.store.firebaseDetail

import androidx.lifecycle.*
import com.project.navermap.data.entity.firebase.FirebaseEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FirebaseDetailViewModel @AssistedInject constructor(
    @Assisted private val firebaseEntity: FirebaseEntity
): ViewModel() {

    /**
     * Hilt 런타임 주입
     */
    @AssistedFactory
    interface FirebaseDetailAssistedFactory {
        fun create(firebaseEntity: FirebaseEntity
        ): FirebaseDetailViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: FirebaseDetailAssistedFactory,
            firebaseEntity: FirebaseEntity
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(firebaseEntity) as T
            }
        }
    }

    private val _storeDetailResultLiveData = MutableLiveData<FirebaseDetailResult>(FirebaseDetailResult.Uninitialized)
    val storeDetailResultLiveData: LiveData<FirebaseDetailResult> get() = _storeDetailResultLiveData

    fun fetchData(): Job = viewModelScope.launch {
        _storeDetailResultLiveData.value = FirebaseDetailResult.Loading
        _storeDetailResultLiveData.value = FirebaseDetailResult.Success(
            firebaseEntity = firebaseEntity
        )
    }
}