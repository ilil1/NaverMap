//package com.project.navermap.presentation.mainActivity.myinfo.like

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.project.navermap.data.repository.Like.LikeListRepository
import com.project.navermap.domain.model.Model
import com.project.navermap.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

//@HiltViewModel
//class LikeListViewModel<T : Model> : BaseViewModel() {
//    private val _likeData = MutableLiveData<LikeState>(LikeState.Uninitialized)
//    val likeData: LiveData<LikeState> = _likeData

  //  override fun fetchData(): Job = viewModelScope.launch {
  //      _likeData.value = LikeState.Loading
  //      _likeData.value = LikeState.Success(
  //          modelList = likeListRepository.getAll()
  //      )
  //  }

 //   fun delete(model: T) = viewModelScope.launch {
 //       likeListRepository.delete(model)
  //      fetchData()
  //  }
//}