package com.project.navermap

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.google.gson.Gson
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.CircleOverlay
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import com.project.navermap.databinding.ActivityMainBinding
import com.project.navermap.databinding.DialogFilterBinding
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var uiScope: CoroutineScope // 코루틴 생명주기 관리
    private var shopList: MutableList<ShopData> = mutableListOf()
    private var markers = mutableListOf<Marker>()

    private lateinit var binding: ActivityMainBinding

    private lateinit var locationSource: FusedLocationSource
    private lateinit var locationManager: LocationManager
    private lateinit var naverMap: NaverMap
    private var infoWindow: InfoWindow? = null
    private lateinit var curLocation: Location

    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: AlertDialog

    private lateinit var chkAll: CheckBox
    private var filterCategoryOptions = mutableListOf<CheckBox>()
    private var filterCategoryChecked = mutableListOf<Boolean>()

    private val GEOCODE_URL = "http://dapi.kakao.com/v2/local/search/address.json?query="
    private val GEOCODE_USER_INFO = ""

    companion object {

        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        private const val DISTANCE = 300
        const val MY_LOCATION_KEY = "MY_LOCATION_KEY"

        private val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

//    private val locationListener: LocationListener by lazy {
//        LocationListener { location -> curLocation = location }
//    }

    private val dialogBinding by lazy {
        val displayRectangle = Rect()
        this.window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        DialogFilterBinding.inflate(layoutInflater).apply {
            root.minimumHeight = (displayRectangle.width() * 0.9f).toInt()
            root.minimumHeight = (displayRectangle.height() * 0.9f).toInt()
        }
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var mapFragment: MapFragment =
            supportFragmentManager.findFragmentById(R.id.map_fragment) as MapFragment

        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

//        locationManager.requestLocationUpdates(
//            LocationManager.GPS_PROVIDER,
//            1000,
//            1f,
//            locationListener
//        )
//
//        locationManager.requestLocationUpdates(
//            LocationManager.NETWORK_PROVIDER,
//            1000,
//            1f,
//            locationListener
//        )

        mapFragment.getMapAsync(this)

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        uiScope = CoroutineScope(Dispatchers.Main)

        getApiShopList()
        initDialog()

        binding.btnSearchAround.setOnClickListener {
            try {
                updateMarker()
            } catch (ex: Exception) {
                Toast.makeText(this, "리스트를 가져오는 중", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnFilter.setOnClickListener {
            dialog = builder.show()
        }

        binding.btnCloseMarkers.setOnClickListener {
            removeAllMarkers()
        }

        binding.etSearch.setOnClickListener {
            openSearchActivityForResult()
        }
    }

    private fun openSearchActivityForResult() {
        startSearchActivityForResult.launch(
            Intent(applicationContext, SearchAddressActivity::class.java)
        )
    }

    private val startSearchActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->

            if (result.resultCode == Activity.RESULT_OK) {

                val bundle = result.data?.extras //인텐트로 보낸 extras를 받아옵니다.
                val str = bundle?.get(MY_LOCATION_KEY).toString()
                var asw : MapSearchInfoEntity?

                Thread {

                    val obj: URL
                    val address: String = URLEncoder.encode(str, "UTF-8")

                    obj = URL(GEOCODE_URL + address)

                    val con: HttpURLConnection = obj.openConnection() as HttpURLConnection

                    con.setRequestMethod("GET")
                    con.setRequestProperty("Authorization", "KakaoAK " + GEOCODE_USER_INFO)
                    con.setRequestProperty("content-type", "application/json")
                    con.setDoOutput(true)
                    con.setUseCaches(false)
                    con.setDefaultUseCaches(false)

                    val data = con.inputStream.bufferedReader().readText()
                    val dataList = "[$data]"
                    val xy = Gson().fromJson(dataList, Array<Address>::class.java).toList()

                    asw = MapSearchInfoEntity(
                        xy[0].documents[0].addressName,
                        xy[0].documents[0].roadAddress.buildingName,
                        LocationLatLngEntity(
                            xy[0].documents[0].y.toDouble(),
                            xy[0].documents[0].x.toDouble()
                        )
                    )

                    runOnUiThread {
                        Toast.makeText(this, asw.toString(), Toast.LENGTH_LONG).show()
                    }

                }.start()
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

        try {
            marker.position = LatLng(37.5670135, 126.9783740)
        } catch (ex: Exception) {
            Toast.makeText(this, "마커를 읽어오는 중", Toast.LENGTH_SHORT).show()
        }
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

    private fun removeAllMarkers() {
        markers.forEach { marker ->
            marker.map = null
        }
        infoWindow?.close()
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

        var markets: List<ShopData>
        var temp = arrayListOf<Marker>()
        var i = 0

        markets = shopList

        markets?.let {
            repeat(markets.size) {

//                val dist = calDist(
//                    curLocation.latitude,
//                    curLocation.longitude,
//                    markets[i].latitude,
//                    markets[i].longitude)

                if (filterCategoryChecked[getCategoryNum(markets[i].category)]) {
                    temp += Marker().apply {
                        position = LatLng(markets[i].latitude, markets[i].longitude)
                        icon = MarkerIcons.BLACK
                        tag = markets[i].shop_name
                        zIndex = i
                    }
                }
                i++
            }
            markers = temp
            searchAround()
        }
    }

    private fun calDist(lat1:Double, lon1:Double, lat2:Double, lon2:Double) : Long {

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

    private fun initDialog() {

        builder = AlertDialog.Builder(this)
        builder.setCancelable(false)

        chkAll = dialogBinding.all

        with(dialogBinding) {
            filterCategoryOptions.addAll(arrayOf(
                foodBeverage, service, fashionAccessories,
                supermarket, fashionClothes, etc)
            )
        }

        chkAll.setOnClickListener {
            filterCategoryOptions.forEach { checkBox ->
                checkBox.isChecked = chkAll.isChecked
            }
        }

        filterCategoryOptions.forEach { checkBox ->
            filterCategoryChecked.add(true)
            checkBox.setOnClickListener {
                for (_checkBox in filterCategoryOptions) {
                    if (!_checkBox.isChecked) {
                        chkAll.isChecked = false
                        return@setOnClickListener
                    }
                }
                chkAll.isChecked = true
            }
        }

        dialogBinding.btnCloseFilter.setOnClickListener {

            var check = true
            for (i in 0 until filterCategoryOptions.size) {
                filterCategoryOptions[i].isChecked = filterCategoryChecked[i]
                if (!filterCategoryOptions[i].isChecked)
                    check = false
            }
            chkAll.isChecked = check

            dialog.dismiss()
            if (dialogBinding.root.parent != null) {
                (dialogBinding.root.parent as ViewGroup).removeView(dialogBinding.root)
            }
        }

        dialogBinding.btnFilterApply.setOnClickListener {

            var noChk = true
            for (item in filterCategoryOptions) {
                if (item.isChecked) {
                    noChk = false
                    break
                }
            }

            if (noChk) {
                Toast.makeText(this, "적어도 하나 이상 카테고리를 선택해야 합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            for (i in 0 until filterCategoryOptions.size)
                filterCategoryChecked[i] = filterCategoryOptions[i].isChecked

            updateMarker()

            dialog.dismiss()
            if (dialogBinding.root.parent != null) {
                (dialogBinding.root.parent as ViewGroup).removeView(dialogBinding.root)
            }
        }

        dialogBinding.btnFilterReset.setOnClickListener {

            filterCategoryOptions.forEach { it.isChecked = true }

            var check = true
            for (item in filterCategoryOptions)
                if (!item.isChecked) {
                    check = false
                }

            if (check) chkAll.isChecked = true
        }

        builder.setView(dialogBinding.root)
        builder.create()
    }
}
