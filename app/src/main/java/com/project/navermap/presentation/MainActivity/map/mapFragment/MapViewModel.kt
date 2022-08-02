package com.project.navermap.presentation.MainActivity.map.mapFragment

import android.content.Context
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
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.MarkerIcons
import com.project.navermap.R
import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.data.entity.ShopInfoEntity
import com.project.navermap.data.repository.ShopApiRepository
import com.project.navermap.domain.usecase.mapViewmodel.GetShopListUseCaseImpl
import com.project.navermap.domain.usecase.mapViewmodel.ShopResult
import kotlinx.coroutines.launch

class MapViewModel
@ViewModelInject
constructor(
    private val getShopListUseCaseImpl : GetShopListUseCaseImpl
) : ViewModel() {

    private var naverMap: NaverMap? = null
    lateinit var destLocation: LocationEntity
    private var markers = mutableListOf<Marker>()
    var infoWindow: InfoWindow? = null

    private var filterCategoryChecked = mutableListOf<Boolean>()

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

    fun SetCategoryChecked(_filterCategoryChecked : MutableList<Boolean>) {
        filterCategoryChecked.clear()
        filterCategoryChecked = _filterCategoryChecked
    }

    fun getCategoryChecked() : MutableList<Boolean> {
        return filterCategoryChecked
    }

    fun loadShopList() = viewModelScope.launch {
        val shopList = getShopListUseCaseImpl.getShopEntityList()
        _data.value = MapState.Success(shopList)
    }

    fun setDestinationLocation(loc: LocationEntity) { destLocation = loc }
    fun getMarkers(): List<Marker>? { return markers }
    fun setMap(m: NaverMap) { naverMap = m }
    fun getMap(): NaverMap? { return naverMap }

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

    fun calDist(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Long {

        val EARTH_R = 6371000.0
        val rad = Math.PI / 180
        val radLat1 = rad * lat1
        val radLat2 = rad * lat2
        val radDist = rad * (lon1 - lon2)

        var distance = Math.sin(radLat1) * Math.sin(radLat2)
        distance = distance + Math.cos(radLat1) * Math.cos(radLat2) * Math.cos(radDist)
        val ret = EARTH_R * Math.acos(distance)

        return Math.round(ret)
    }


    fun getShopEntityList(): List<ShopInfoEntity>? {
        when (data.value) {
            is MapState.Success -> {
                return (data.value as MapState.Success).shopInfoList
            }
        }
        return null
    }

    fun getCategoryNum(category: String): Int =
        when (category) {
            "FOOD_BEVERAGE" -> 0
            "SERVICE" -> 1
            "ACCESSORY" -> 2
            "MART" -> 3
            "FASHION" -> 4
            else -> 5
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

    /**
     * 네이버 지도상에 마커를 표시
     */
    fun showMarkersOnMap() {
        if (markers.isNullOrEmpty())
            return
        for (marker in markers) {
            marker.map = getMap()
            setMarkerIconAndColor(marker, getCategoryNum(getShopEntityList()?.get(marker.zIndex)!!.category))
        }
    }

    fun setMarkerIconAndColor(marker: Marker, category: Int) = with(marker) {
        when (category) {
            0 -> {
                icon = OverlayImage.fromResource(R.drawable.marker_m)
                iconTintColor = Color.parseColor("#46F5FF")
            }
            1 -> {
                icon = OverlayImage.fromResource(R.drawable.marker_r)
                iconTintColor = Color.parseColor("#FFCB41")
            }
            2 -> {
                icon = OverlayImage.fromResource(R.drawable.marker_s)
                iconTintColor = Color.parseColor("#886AFF")
            }
            3 -> {
                icon = OverlayImage.fromResource(R.drawable.marker_e)
                iconTintColor = Color.parseColor("#04B404")
            }
            4 -> {
                icon = OverlayImage.fromResource(R.drawable.marker_f)
                iconTintColor = Color.parseColor("#8A0886")
            }
            5 -> {
                icon = OverlayImage.fromResource(R.drawable.marker_f)
                iconTintColor = Color.parseColor("#0B2F3A")
            }
        }
    }

    fun setMarkerListener(context : Context) {
        for (marker in getMarkers()!!) {
            var tempinfoWindow = InfoWindow()
            tempinfoWindow?.adapter = object : InfoWindow.DefaultTextAdapter(context) {
                override fun getText(infoWindow: InfoWindow): CharSequence {
                    return infoWindow.marker?.tag as CharSequence
                }
            }
            infoWindow = tempinfoWindow
            marker.setOnClickListener {

                if (tempinfoWindow?.marker != null) {
                    tempinfoWindow?.close()
                } else {
                    tempinfoWindow?.open(marker)
                }

                // 여기서 오픈한 말풍선은 fbtnViewPager2를 클릭하면 제거
//                viewPagerAdapter.registerStore(markets[marker.zIndex])
//                binding.viewPager2.adapter = viewPagerAdapter
//                binding.viewPager2.visibility = View.VISIBLE
//                binding.fbtnCloseViewPager.visibility = View.VISIBLE
                true
            }
        }
    }


    fun setMarkers(list: List<Marker>) {
        markers.clear()
        markers = list as MutableList
    }

    fun updateMarker(context : Context) {
        deleteMarkers()

        var temp = arrayListOf<Marker>()
        val shopList = getShopEntityList()!!
        var i = 0

        shopList.let {
            repeat(shopList.size) {
                if (filterCategoryChecked[getCategoryNum(shopList[i].category)]) {
                    temp += Marker().apply {
                        position = LatLng(shopList[i].latitude, shopList[i].longitude)
                        icon = MarkerIcons.BLACK
                        tag = shopList[i].shop_name
                        zIndex = i
                    }
                }
                i++
            }
            setMarkers(temp)
            deleteMarkers()
        }
        showMarkersOnMap()
        setMarkerListener(context)
    }

    fun removeAllMarkers() {
        getMarkers()!!.forEach { marker ->
            marker.map = null
        }
        infoWindow?.close()
    }
}