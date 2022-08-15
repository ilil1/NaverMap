package com.project.navermap.presentation.MainActivity.home

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.navermap.R
import com.project.navermap.data.repository.home.HomeRepository
import com.project.navermap.data.repository.suggest.SuggestRepository
import com.project.navermap.domain.model.CellType
import com.project.navermap.domain.model.HomeItemModel
import com.project.navermap.domain.model.SuggestItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val suggestRepository: SuggestRepository
) : ViewModel() {

    private val _marketData = MutableLiveData<HomeMainState>(HomeMainState.Uninitialized)
    val marketData: LiveData<HomeMainState> = _marketData

    private val _suggestData = MutableLiveData<HomeMainState>(HomeMainState.Uninitialized)
    val suggestData: LiveData<HomeMainState> = _suggestData

    private val _seasonData = MutableLiveData<HomeMainState>(HomeMainState.Uninitialized)
    val seasonData: LiveData<HomeMainState> = _seasonData

    private val _fixData = MutableLiveData<HomeMainState>(HomeMainState.Uninitialized)
    val fixData: LiveData<HomeMainState> = _fixData

    private val _slidData = MutableLiveData<Drawable>()
    val slideData: LiveData<Drawable> = _slidData

    private val _annivalData = MutableLiveData<HomeMainState>(HomeMainState.Uninitialized)
    val annivalData: LiveData<HomeMainState> = _annivalData


    private lateinit var allNewSaleItemsList: List<HomeItemModel>
    private lateinit var suggestItemList: List<SuggestItemModel>

    fun fetchData(): Job = viewModelScope.launch {
        // 더 이상 fetchData가 initState에서 실행되지 않고 위치 정보를 불러온 뒤에
        // 실행 되므로 위치 정보를 불러왔는지 확인할 필요가 없음
        fetchMarketData()
        //   fetchItemData()
        fetchHobbyMarket()
        fetchSeasonMarket()
        fetchAnnivalMarket()
        slideImage()
    }

    private suspend fun slideImage() {
        _slidData.value = BitmapDrawable(BitmapFactory.decodeFile(R.drawable.airfilter.toString()))
    }

    private suspend fun fetchSeasonMarket() {
        if (seasonData.value !is HomeMainState.Success<*>) {
            _seasonData.value = HomeMainState.Loading
            _seasonData.value = HomeMainState.Success(
                modelList = suggestRepository.seasonMarket()
            )
        }
    }

    private suspend fun fetchAnnivalMarket() {
        if (annivalData.value !is HomeMainState.Success<*>) {
            _annivalData.value = HomeMainState.Loading

            _annivalData.value = HomeMainState.Success(
                modelList = suggestRepository.suggestAnniversary()
            )
        }
    }


    private suspend fun fetchHobbyMarket() {
        if (suggestData.value !is HomeMainState.Success<*>) {
            _suggestData.value = HomeMainState.Loading

            _suggestData.value = HomeMainState.Success(
                modelList = suggestRepository.suggestHobby()
            )
        }
    }

    private suspend fun fetchMarketData() {
        if (marketData.value !is HomeMainState.Success<*>) {
            _marketData.value = HomeMainState.Loading

            // sorted by distance
            _marketData.value = HomeMainState.Success(
                modelList = homeRepository.getAllMarketList().map {
                    it.copy(type = CellType.HOME_MAIN_MARKET_CELL)
                }.sortedBy { it.distance }
            )
        }
    }
}