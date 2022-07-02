package com.project.navermap

import android.Manifest
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.CircleOverlay
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import com.project.navermap.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var uiScope: CoroutineScope // 코루틴 생명주기 관리
    private var shopList: MutableList<ShopData> = mutableListOf()
    private var markers = mutableListOf<Marker>()

    private lateinit var binding: ActivityMainBinding

    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private var infoWindow: InfoWindow? = null

    companion object {

        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        private const val DISTANCE = 300

        private val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var mapFragment: MapFragment =
            supportFragmentManager.findFragmentById(R.id.map_fragment) as MapFragment
        mapFragment.getMapAsync(this)

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        uiScope = CoroutineScope(Dispatchers.Main)

        getApiShopList()

        binding.btnSearchAround.setOnClickListener {
            try {
                updateMarker()
            } catch (ex: Exception) {
                Toast.makeText(this, "리스트를 가져오는 중", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.d("onRequest", "onRequestPermissionsResult")

        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated) { // 권한 거부됨
                Log.d("권한 거부", "권한 거부됨")
                naverMap.locationTrackingMode = LocationTrackingMode.None
            } else {
                Log.d("권한 승인", "권한 승인됨")
                naverMap.locationTrackingMode = LocationTrackingMode.Follow // 현위치 버튼 컨트롤 활성
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(map: NaverMap) {

        this.naverMap = map

        naverMap.uiSettings.isLocationButtonEnabled = true
        naverMap.uiSettings.isScaleBarEnabled = true //축적바 기본값은 true

        naverMap.locationSource = locationSource

        ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE)

        val marker: Marker = Marker(MarkerIcons.BLACK).apply {
            zIndex = 111
            iconTintColor = Color.parseColor("#FA295B")
            width = 100
            height = 125
        }

        marker.position = LatLng(37.5670135, 126.9783740)
        marker.map = naverMap

        val cameraUpdate = CameraUpdate.scrollTo(LatLng(37.5670135, 126.9783740))
        naverMap.moveCamera(cameraUpdate)

        marker.setOnClickListener {
            this.infoWindow?.close()
            this.infoWindow = InfoWindow()
            this.infoWindow?.open(marker)
            true
        }

        val circle = CircleOverlay()
        circle.center = LatLng(37.5670135, 126.9783740)
        circle.radius = DISTANCE.toDouble()

        circle.outlineWidth = 1
        circle.outlineColor = Color.parseColor("#AC97FE")
        circle.zIndex = 100
        circle.map = naverMap

        Toast.makeText(this, "맵 초기화 완료", Toast.LENGTH_LONG).show()
    }

    fun getApiShopList() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val response = RetrofitUtil.shopController.getList()
                if (response.isSuccessful) {
                    val list = response.body()
                    list?.let {
                        it.shopList.forEach { ShopData ->
                            shopList.add(ShopData)
                        }
                    }
                }
                else {
                    null
                }
            }
        }
    }

    private fun setMarkerIconAndColor(marker: Marker, category: Int) = with(marker) {
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

    private fun getCategoryNum(category: String): Int =
        when(category) {
            "FOOD_BEVERAGE" -> 0
            "SERVICE" -> 1
            "ACCESSORY" -> 2
            "MART" -> 3
            "FASHION" -> 4
            else -> 5
        }

    private fun searchAround() {

        deleteMarkers()
        for (marker in markers) {
            marker.map = naverMap
            setMarkerIconAndColor(marker, getCategoryNum(shopList.get(marker.zIndex)!!.category))
        }
    }

    private fun deleteMarkers() {
        for (marker in markers) {
            marker.map = null
        }
    }

    private fun updateMarker() {

        deleteMarkers()

        var markets: List<ShopData> = mutableListOf()
        var temp = arrayListOf<Marker>()
        var i = 0

        markets = shopList

        markets?.let {
            repeat(markets.size) {
                temp += Marker().apply {
                    position = LatLng(markets[i].latitude, markets[i].longitude)
                    icon = MarkerIcons.BLACK
                    tag = markets[i].shop_name
                    zIndex = i
                }
                i++
            }
            markers = temp
            searchAround()
        }
    }
}
