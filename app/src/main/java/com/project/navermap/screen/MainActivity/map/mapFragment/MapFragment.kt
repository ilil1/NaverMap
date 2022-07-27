package com.project.navermap.screen.MainActivity.map.mapFragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.gson.Gson
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.CircleOverlay
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import com.project.navermap.*
import com.project.navermap.R
import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.data.entity.MapSearchInfoEntity
import com.project.navermap.data.entity.ShopInfoEntity
import com.project.navermap.databinding.DialogFilterBinding
import com.project.navermap.databinding.FragmentMapBinding
import com.project.navermap.screen.MainActivity.MainActivity
import com.project.navermap.screen.MainActivity.MainState
import com.project.navermap.screen.MainActivity.MainViewModel
import com.project.navermap.screen.MainActivity.map.SearchAddress.SearchAddressActivity
import com.project.navermap.util.RetrofitUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

@AndroidEntryPoint
class MapFragment : Fragment() , OnMapReadyCallback {

    private val activityViewModel by activityViewModels<MainViewModel>()
    private val viewModel: MapViewModel by viewModels()
    private var markets : List<ShopInfoEntity> = mutableListOf() //뷰모델에 넣어야하는지 고민

    private var infoWindow: InfoWindow? = null
    private lateinit var binding: FragmentMapBinding

    private lateinit var locationSource: FusedLocationSource
    private lateinit var locationManager: LocationManager
    private lateinit var naverMap: NaverMap

    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: AlertDialog
    //private lateinit var dialog: Dialog

    private lateinit var chkAll: CheckBox
    private var filterCategoryOptions = mutableListOf<CheckBox>()
    private var filterCategoryChecked = mutableListOf<Boolean>()

    private val GEOCODE_URL = "http://dapi.kakao.com/v2/local/search/address.json?query="
    private val GEOCODE_USER_INFO = "2b4e5d3d2f35dd584b398978c3aca53a"

    companion object {

        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        private const val DISTANCE = 300
        const val MY_LOCATION_KEY = "MY_LOCATION_KEY"

        private val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    private val locationListener: LocationListener by lazy {
        LocationListener { location ->
            activityViewModel.curLocation = location
        }
    }

    private val dialogBinding by lazy {
        val displayRectangle = Rect()
        requireActivity().window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        DialogFilterBinding.inflate(layoutInflater).apply {
            root.minimumHeight = (displayRectangle.width() * 0.9f).toInt()
            root.minimumHeight = (displayRectangle.height() * 0.9f).toInt()
        }
    }

    fun observeData() {

        viewModel.data.observe(viewLifecycleOwner) {
            when (it) {
                is MapState.Uninitialized -> {
                    viewModel.getApiShopList()
                }
                is MapState.Loading -> {}
                is MapState.Success -> {
                    markets = viewModel.getShopEntityList()!!
                }
                is MapState.Error -> {}
            }
        }

        activityViewModel.locationData.observe(viewLifecycleOwner) {
            when (it) {
                is MainState.Uninitialized -> {}
                is MainState.Loading -> {}
                is MainState.Success -> {
                    viewModel.updateLocation(it.mapSearchInfoEntity.locationLatLng)
                    removeAllMarkers()
                }
                is MainState.Error -> {}
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

        initDialog()
        initMap()
        observeData()

        binding.btnCurLocation.setOnClickListener {

            try {
                viewModel.getMap()?.cameraPosition = CameraPosition(
                    LatLng(activityViewModel.getCurrentLocation().latitude,
                        activityViewModel.getCurrentLocation().longitude),
                    15.0)

            } catch (ex: Exception) {
                Toast.makeText(context, "CurLocation 초기화 중", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnDestLocation.setOnClickListener {

            try {
                viewModel.getMap()?.cameraPosition = CameraPosition(
                    LatLng(activityViewModel.getDestinationLocation().latitude,
                        activityViewModel.getDestinationLocation().longitude),
                    15.0)

                viewModel.updateLocation(activityViewModel.getDestinationLocation())

            } catch (ex: Exception) {
                Toast.makeText(context, "DestLocation 초기화 중", Toast.LENGTH_SHORT).show()
            }
        }

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

    @SuppressLint("MissingPermission")
    private fun initMap() = with(binding) {

        locationSource = FusedLocationSource(this@MapFragment, LOCATION_PERMISSION_REQUEST_CODE)

        locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000,
            1f,
            locationListener)

        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            1000,
            1f,
            locationListener)
    }

    private fun init() {

//        webViewAddress = // 메인 웹뷰
//        webViewLayout = // 웹뷰가 속한 레이아웃
//        공통 설정

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



    private fun setMarkerListener(markets: List<ShopInfoEntity>) {
        for (marker in viewModel.getMarkers()!!) {

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

                // 여기서 오픈한 말풍선은 fbtnViewPager2를 클릭하면 제거
                //viewPagerAdapter.registerStore(markets[marker.zIndex])
                //binding.viewPager2.adapter = viewPagerAdapter
                //binding.viewPager2.visibility = View.VISIBLE
                //binding.fbtnCloseViewPager.visibility = View.VISIBLE
                true
            }
        }
    }

    private fun removeAllMarkers() {
        viewModel.getMarkers()!!.forEach { marker ->
            marker.map = null
        }
        infoWindow?.close()
    }

    private fun updateMarker() {

        viewModel.deleteMarkers()

        var temp = arrayListOf<Marker>()
        var i = 0

        markets?.let {
            repeat(markets.size) {
                if (filterCategoryChecked[viewModel.getCategoryNum(markets[i].category)]) {
                    temp += Marker().apply {
                        position = LatLng(markets[i].latitude, markets[i].longitude)
                        icon = MarkerIcons.BLACK
                        tag = markets[i].shop_name
                        zIndex = i
                    }
                }
                i++
            }
            viewModel.setMarkers(temp)
            viewModel.deleteMarkers()
            viewModel.showMarkersOnMap()

            setMarkerListener(markets)
        }
    }

    override fun onMapReady(map: NaverMap) {

        this.naverMap = map.apply {
            this.locationSource = this@MapFragment.locationSource //현재 위치값을 넘긴다
            locationTrackingMode = LocationTrackingMode.NoFollow
            uiSettings.isLocationButtonEnabled = true
            uiSettings.isScaleBarEnabled = true
            uiSettings.isCompassEnabled = true
        }

        viewModel.setMap(naverMap)

        try {//중복코드로 리팩토링으로 제거 필요함
            viewModel.firstupdateLocation()
        } catch (ex: Exception) {
            Toast.makeText(context, "위치 초기화 중", Toast.LENGTH_SHORT).show()
        }

        Toast.makeText(requireContext(), "맵 초기화 완료", Toast.LENGTH_LONG).show()
    }
}