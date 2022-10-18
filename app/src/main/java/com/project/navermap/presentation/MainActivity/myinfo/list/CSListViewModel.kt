package com.project.navermap.presentation.MainActivity.myinfo.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.project.navermap.data.repository.myinfo.CSRepository
import com.project.navermap.domain.model.CSModel
import com.project.navermap.domain.model.category.CSCategory
import com.project.navermap.presentation.base.BaseViewModel
import dagger.Binds
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CSListViewModel @Inject constructor(
    private val csCategory: CSCategory,
    private val csRepository: CSRepository
) : BaseViewModel() {
    private val _csListData = MutableLiveData<List<CSModel>>()
    val csListData: LiveData<List<CSModel>>
        get() = _csListData


    override fun fetchData(): Job = viewModelScope.launch {
        _csListData.value = when (csCategory) {
            CSCategory.LOGIN -> csRepository.findCsByCategory(CSCategory.LOGIN)
            CSCategory.REVIEW -> csRepository.findCsByCategory(CSCategory.REVIEW)
            CSCategory.ORDER -> csRepository.findCsByCategory(CSCategory.ORDER)
            CSCategory.USE -> csRepository.findCsByCategory(CSCategory.USE)
            CSCategory.ETC -> csRepository.findCsByCategory(CSCategory.ETC)
        }
    }


}