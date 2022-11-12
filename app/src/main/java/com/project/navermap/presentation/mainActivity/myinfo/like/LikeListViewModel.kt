//package com.project.navermap.presentation.mainActivity.myinfo.like
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.project.navermap.domain.usecase.mapViewmodel.legacyShop.GetShopListUseCaseImpl
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class LikeListViewModel @Inject constructor(
//   private val getShopListUseCaseImple : GetShopListUseCaseImpl
//) : ViewModel() {
//    private val _likeData = MutableLiveData<LikeState>(LikeState.Uninitialized)
//    val likeData: LiveData<LikeState> = _likeData
//
//     fun fetchData(): Job = viewModelScope.launch {
//        _likeData.value = LikeState.Loading
//
//    }
//
////    fun delete() = viewModelScope.launch {
////        getShopListUseCaseImple.
////        fetchData()
////    }
//}