package com.project.navermap.screen.map.mapFragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Message
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHost
import com.google.gson.Gson
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.MapFragment
import com.naver.maps.map.overlay.CircleOverlay
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import com.project.navermap.*
import com.project.navermap.R
import com.project.navermap.databinding.ActivityMainBinding
import com.project.navermap.databinding.DialogFilterBinding
import com.project.navermap.databinding.FragmentMapBinding
import com.project.navermap.screen.map.mapLocationSetting.MapLocationSettingActivity
import com.project.navermap.screen.map.myLocation.MyLocationActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class MapFragment : Fragment() , OnMapReadyCallback {

    private lateinit var uiScope: CoroutineScope // 코루틴 생명주기 관리
    private var shopList: MutableList<ShopData> = mutableListOf()
    private var markers = mutableListOf<Marker>()
    private var infoWindow: InfoWindow? = null
    private lateinit var binding: FragmentMapBinding

    private lateinit var locationSource: FusedLocationSource
    private lateinit var locationManager: LocationManager
    private lateinit var naverMap: NaverMap

    private lateinit var curLocation: Location

    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: AlertDialog
    //private lateinit var dialog: Dialog

    private lateinit var chkAll: CheckBox
    private var filterCategoryOptions = mutableListOf<CheckBox>()
    private var filterCategoryChecked = mutableListOf<Boolean>()

    private val GEOCODE_URL = "http://dapi.kakao.com/v2/local/search/address.json?query="
    private val GEOCODE_USER_INFO = "2b4e5d3d2f35dd584b398978c3aca53a"

    private lateinit var mapSearchInfoEntity : MapSearchInfoEntity

    companion object {

        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        private const val DISTANCE = 300
        const val MY_LOCATION_KEY = "MY_LOCATION_KEY"

        private val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    private lateinit var locationListener: LocationListener

    private val dialogBinding by lazy {
        val displayRectangle = Rect()
        requireActivity().window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        DialogFilterBinding.inflate(layoutInflater).apply {
            root.minimumHeight = (displayRectangle.width() * 0.9f).toInt()
            root.minimumHeight = (displayRectangle.height() * 0.9f).toInt()
        }
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {

                val bundle = result.data?.extras
                val result = bundle?.get("result")

                Toast.makeText(requireContext(), result.toString(), Toast.LENGTH_LONG).show()
            }
        }

    private val changeLocationLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { results ->
            results.data?.getParcelableExtra<MapSearchInfoEntity>(
                MapLocationSettingActivity.MY_LOCATION_KEY)
                ?.let { mapSearchInfoEntity ->
                    //getReverseGeoInformation(mapSearchInfoEntity.locationLatLng)
                    //setDestinationLocation(mapSearchInfoEntity.locationLatLng)
                }
        }

    private val myLocationStartForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {

            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun getReverseGeoInformation(locationLatLngEntity: LocationEntity) {

        uiScope.launch {
            withContext(Dispatchers.IO) {

                val currentLocation = locationLatLngEntity
                val response = RetrofitUtil.mapApiService.getReverseGeoCode(
                    lat = locationLatLngEntity.latitude,
                    lon = locationLatLngEntity.longitude
                )//response = addressInfo

                if (response.isSuccessful) {
                    val body = response.body()
                    withContext(Dispatchers.Main) {
                        mapSearchInfoEntity = MapSearchInfoEntity(
                            fullAddress = body!!.addressInfo.fullAddress ?: "주소 정보 없음",
                            name = body!!.addressInfo.buildingName ?: "주소 정보 없음",
                            locationLatLng = currentLocation
                        )
                    }
                } else {
                    null
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMapBinding.inflate(layoutInflater)

        binding.mapView.getMapAsync(this@MapFragment)

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        uiScope = CoroutineScope(Dispatchers.Main)

        getApiShopList()
        initDialog()

        binding.btnSearchAround.setOnClickListener {
            try {
                updateMarker()
            } catch (ex: Exception) {
                Toast.makeText(requireContext(), "리스트를 가져오는 중", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnFilter.setOnClickListener {
            dialog = builder.show()
            //dialog.show()
        }

        binding.btnCloseMarkers.setOnClickListener {
            removeAllMarkers()
        }

        binding.etSearch.setOnClickListener {
            init()
            openSearchActivityForResult()
        }

        binding.TmapBtn.setOnClickListener {
            try {
                startForResult.launch(
                    MapLocationSettingActivity.newIntent(requireContext(), mapSearchInfoEntity)
                )
            } catch (ex: Exception) {
                Toast.makeText(requireContext(), "initMap() 초기화 중", Toast.LENGTH_SHORT).show()
            }
        }

        locationListener = LocationListener { location ->
            curLocation = location

            val locationEntity = LocationEntity(
                latitude = location.latitude,
                longitude = location.longitude)

            getReverseGeoInformation(locationEntity)

            Toast.makeText(requireContext(), "curLocation 초기화 완료", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private val startSearchActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->

            if (result.resultCode == AppCompatActivity.RESULT_OK) {

                val bundle = result.data?.extras //인텐트로 보낸 extras를 받아옵니다.
                val str = bundle?.get(MainActivity.MY_LOCATION_KEY).toString()
                var asw: MapSearchInfoEntity?

                Log.d("SearchActivityForResult", str)

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

                    Log.d("application/json", data)

                    val dataList = "[$data]"
                    val xy = Gson().fromJson(dataList, Array<Address>::class.java).toList()

                    asw = MapSearchInfoEntity(
                        xy[0].documents[0].addressName,
                        xy[0].documents[0].roadAddress.buildingName,
                        LocationEntity(
                            xy[0].documents[0].y.toDouble(),
                            xy[0].documents[0].x.toDouble()
                        )
                    )

                    activity?.runOnUiThread {
                        Toast.makeText(requireContext(), asw.toString(), Toast.LENGTH_LONG).show()
                    }

                }.start()
            }
        }


    private fun openSearchActivityForResult() {
        startSearchActivityForResult.launch(
            Intent(requireContext(), SearchAddressActivity::class.java)
        )
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
                naverMap.locationTrackingMode = LocationTrackingMode.Follow // 현위치 버튼 컨트롤
                //initMap()
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

        requestPermissions(PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE)

        val marker: Marker = Marker(MarkerIcons.BLACK).apply {
            zIndex = 111
            iconTintColor = Color.parseColor("#FA295B")
            width = 100
            height = 125
        }

        try {
            marker.position = LatLng(37.5670135, 126.9783740)
        } catch (ex: Exception) {
            Toast.makeText(requireContext(), "마커를 읽어오는 중", Toast.LENGTH_SHORT).show()
        }
        marker.map = naverMap

        val cameraUpdate = CameraUpdate.scrollTo(LatLng(37.5670135, 126.9783740))
        naverMap.moveCamera(cameraUpdate)

        marker.setOnClickListener {
            this.infoWindow?.close()
            this.infoWindow = InfoWindow()
            this.infoWindow?.adapter = object : InfoWindow.DefaultTextAdapter(requireContext()) {
                override fun getText(infoWindow: InfoWindow): CharSequence {
                    return "정보 창 내용"
                }
            }
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

        Toast.makeText(requireContext(), "맵 초기화 완료", Toast.LENGTH_LONG).show()
    }

    private fun init() {

//        webViewAddress = // 메인 웹뷰
//        webViewLayout = // 웹뷰가 속한 레이아웃
// 공통 설정
        binding.webViewAddress.settings.run {
            javaScriptEnabled = true// javaScript 허용으로 메인 페이지 띄움
            javaScriptCanOpenWindowsAutomatically = true//javaScript window.open 허용
            setSupportMultipleWindows(true)
        }

        binding.webViewAddress.addJavascriptInterface(AndroidBridge(), "TestApp")
        binding.webViewAddress.loadUrl("")
        binding.webViewAddress.webChromeClient = webChromeClient
    }

    private inner class AndroidBridge {
        // 웹에서 JavaScript로 android 함수를 호출할 수 있도록 도와줌
        @JavascriptInterface
        fun setAddress(arg1: String?, arg2: String?, arg3: String?) {
            // search.php에서 callback 호출되는 함수

            Log.d("arg1.toString()", arg1.toString())
            Log.d("arg2.toString()", arg2.toString())
            Log.d("arg3.toString()", arg3.toString())

            val str = String.format("%s %s", arg2, arg3)
            var asw: MapSearchInfoEntity?

            Log.d("SearchActivityForResult", str)

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

                Log.d("application/json", data)

                val dataList = "[$data]"
                val xy = Gson().fromJson(dataList, Array<Address>::class.java).toList()

                asw = MapSearchInfoEntity(
                    xy[0].documents[0].addressName,
                    xy[0].documents[0].roadAddress.buildingName,
                    LocationEntity(
                        xy[0].documents[0].y.toDouble(),
                        xy[0].documents[0].x.toDouble()
                    )
                )

                activity?.runOnUiThread {
                    Toast.makeText(requireContext(), asw.toString(), Toast.LENGTH_LONG).show()
                }

            }.start()
        }
    }

    private val webChromeClient = object : WebChromeClient() {

        /// ---------- 팝업 열기 ----------
        /// - 카카오 JavaScript SDK의 로그인 기능은 popup을 이용합니다.
        /// - window.open() 호출 시 별도 팝업 webview가 생성되어야 합니다.
        ///
        lateinit var dialog: Dialog

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreateWindow(
            view: WebView, isDialog: Boolean,
            isUserGesture: Boolean, resultMsg: Message
        ): Boolean {
            // 웹뷰 만들기
            var childWebView = WebView(view.context)
            Log.d("TAG", "웹뷰 만들기")
            // 부모 웹뷰와 동일하게 웹뷰 설정
            childWebView.run {
                settings.run {
                    javaScriptEnabled = true
                    javaScriptCanOpenWindowsAutomatically = true
                    setSupportMultipleWindows(true)
                }
                layoutParams = view.layoutParams
                webViewClient = view.webViewClient
                webChromeClient = view.webChromeClient
            }

            dialog = Dialog(requireContext()).apply {
                setContentView(childWebView)
                window!!.attributes.width = ViewGroup.LayoutParams.MATCH_PARENT
                window!!.attributes.height = ViewGroup.LayoutParams.MATCH_PARENT
                show()
            }

            // TODO: 화면 추가 이외에 onBackPressed() 와 같이
            //       사용자의 내비게이션 액션 처리를 위해
            //       별도 웹뷰 관리를 권장함
            //   ex) childWebViewList.add(childWebView)

            // 웹뷰 간 연동
            val transport = resultMsg.obj as WebView.WebViewTransport
            transport.webView = childWebView
            resultMsg.sendToTarget()

            return true
        }

        override fun onCloseWindow(window: WebView) {
            super.onCloseWindow(window)
            Log.d("로그 ", "onCloseWindow")
            dialog.dismiss()
            // 화면에서 제거하기
            // TODO: 화면 제거 이외에 onBackPressed() 와 같이
            //       사용자의 내비게이션 액션 처리를 위해
            //       별도 웹뷰 array 관리를 권장함
            //   ex) childWebViewList.remove(childWebView)
        }
    }

    private fun initDialog() {

        //dialog = Dialog(this)
        //dialog.setCancelable(false)

        builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(false)

//        val dialog = Dialog(context)
//        dialog.setContentView(R.layout.dialog_update)
//        val width = WindowManager.LayoutParams.MATCH_PARENT
//        val height = WindowManager.LayoutParams.WRAP_CONTENT
//        dialog.window!!.setLayout(width, height)
//        dialog.show()

        chkAll = dialogBinding.all

        with(dialogBinding) {
            filterCategoryOptions.addAll(
                arrayOf(
                    foodBeverage, service, fashionAccessories,
                    supermarket, fashionClothes, etc
                )
            )
        }

        chkAll.setOnClickListener {
            filterCategoryOptions.forEach { checkBox ->
                checkBox.isChecked = chkAll.isChecked
            }
        }

        filterCategoryOptions.forEach { checkBox ->
            filterCategoryChecked.add(true) // btnclose 할 시 ture 반환을 위해서
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
            (dialogBinding.root.parent as ViewGroup).removeView(dialogBinding.root)
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

        dialogBinding.btnFilterApply.setOnClickListener {

            var noChk = true
            for (item in filterCategoryOptions) {
                if (item.isChecked) {
                    noChk = false
                    break
                }
            }

            if (noChk) {
                Toast.makeText(requireContext(), "적어도 하나 이상 카테고리를 선택해야 합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            for (i in 0 until filterCategoryOptions.size)
                filterCategoryChecked[i] = filterCategoryOptions[i].isChecked

            updateMarker()

            dialog.dismiss()
            (dialogBinding.root.parent as ViewGroup).removeView(dialogBinding.root)
        }

        //dialog.setContentView(dialogBinding.root)

        builder.setView(dialogBinding.root)
        builder.create()
    }

    private fun calDist(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Long {

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

    private fun setMarkerListener() {
        for (marker in markers) {

            var tempinfoWindow = InfoWindow()
            tempinfoWindow?.adapter = object : InfoWindow.DefaultTextAdapter(requireContext()) {
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
                true
            }
        }
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
                } else {
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
        when (category) {
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
            setMarkerListener()
        }
    }
}