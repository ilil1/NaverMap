package com.project.navermap.presentation.mainActivity.myinfo.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.navermap.data.repository.myinfo.CSRepository
import com.project.navermap.domain.model.CSModel
import com.project.navermap.domain.model.category.CSCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CSListViewModel @Inject constructor(
    private val csRepository: CSRepository
) : ViewModel() {

    private val _csListData = MutableLiveData<List<CSModel>>()
    val csListData: LiveData<List<CSModel>>
        get() = _csListData

    fun fetchData(csCategory: CSCategory): Job = viewModelScope.launch {
        _csListData.value = csRepository.findCsByCategory(csCategory)
    }
}