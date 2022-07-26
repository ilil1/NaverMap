package com.project.navermap.screen.MainActivity.map.mapFragment

import android.graphics.Color
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons
import com.project.navermap.RetrofitUtil
import com.project.navermap.ShopData
import com.project.navermap.data.entity.AddressHistoryEntity
import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.data.entity.ShopInfoEntity
import com.project.navermap.data.repository.AddressHistoryRepository
import com.project.navermap.data.repository.MapApiRepository
import com.project.navermap.data.repository.ShopApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapViewModel
@ViewModelInject
constructor(
    private val ShopApiRepositoryImpl : ShopApiRepository
) : ViewModel() {

    lateinit var destLocation: LocationEntity
    private var naverMap: NaverMap? = null
    private var markers = mutableListOf<Marker>()

    private var shopList: MutableList<ShopInfoEntity> = mutableListOf()

    private val _data = MutableLiveData<MapState>(MapState.Uninitialized)
    val data: LiveData<MapState> = _data

    private var destMarker: Marker = Marker(
        MarkerIcons.BLACK
    ).apply {
        zIndex = 111
        iconTintColor = Color.parseColor("#FA295B")
        width = 100
        height = 125
    }
        get() = field.apply {
            position = LatLng(
                destLocation.latitude,
                destLocation.longitude
            )
        }


    fun setDestinationLocation(loc: LocationEntity) {
        destLocation = loc
    }

    fun setMap(m: NaverMap) {
        naverMap = m
    }

    fun getMap(): NaverMap? {
        return naverMap
    }

//    fun getApiShopList() = viewModelScope.launch {
//        val list = ShopApiRepositoryImpl.getShopList()
//        list?.let { shopInfoResult ->
//            shopInfoResult.shopList.forEach { ShopData ->
//                shopList.add(ShopData)
//            }
//            Log.d("getApiShopList", shopInfoResult.toString())
//        }
//    }

    fun getApiShopList() = viewModelScope.launch {
        val list = ShopApiRepositoryImpl.getShopList()?.shopList
        list?.let { shopInfoResult ->
            for (i: Int in 0..list.size - 1) {
                shopList.add(
                    ShopInfoEntity(
                        shop_id = shopInfoResult[i].shop_id,
                        shop_name = shopInfoResult[i].shop_name,
                        is_open = shopInfoResult[i].is_open,
                        lot_number_address = shopInfoResult[i].lot_number_address,
                        road_name_address = shopInfoResult[i].road_name_address,
                        latitude = shopInfoResult[i].latitude,
                        longitude = shopInfoResult[i].longitude,
                        average_score = shopInfoResult[i].average_score,
                        review_number = shopInfoResult[i].review_number,
                        main_image = shopInfoResult[i].main_image,
                        description = shopInfoResult[i].description,
                        category = shopInfoResult[i].category,
                        detail_category = shopInfoResult[i].detail_category,
                        is_branch = shopInfoResult[i].is_branch,
                        branch_name = shopInfoResult[i].branch_name
                    )
                )
            }
            _data.value = MapState.Success(shopList)
        }
    }
    /**
     * 네이버 지도상 마커를 모두 없애는 method
     */
    fun deleteMarkers() {
        if (markers.isNullOrEmpty())
            return
        for (marker in markers) {
            marker.map = null
        }
    }

    fun firstupdateLocation() {
        naverMap?.cameraPosition = CameraPosition(
            LatLng(
                destLocation.latitude,
                destLocation.longitude
            ), 15.0
        )
        destMarker.map = naverMap
    }

    fun updateLocation(location: LocationEntity) {
        // 위치 업데이트 될 때마다 목적지 마커 초기화
        destLocation = location
        deleteMarkers()
        naverMap?.cameraPosition = CameraPosition(
            LatLng(
                destLocation.latitude,
                destLocation.longitude
            ), 15.0
        )
        destMarker.map = naverMap
    }
}