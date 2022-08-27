package com.project.navermap.presentation.mainActivity.map.mapFragment

import android.app.Dialog
import android.os.Build
import android.os.Message
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.gson.Gson
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.project.navermap.*
import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.data.entity.MapSearchInfoEntity
import com.project.navermap.data.url.Url
import com.project.navermap.databinding.FragmentMapBinding
import com.project.navermap.domain.model.FoodModel
import com.project.navermap.domain.model.RestaurantModel
import com.project.navermap.extensions.showToast
import com.project.navermap.presentation.mainActivity.MainActivity
import com.project.navermap.presentation.mainActivity.MainState
import com.project.navermap.presentation.mainActivity.MainViewModel
import com.project.navermap.presentation.mainActivity.map.mapFragment.navermap.MarkerClickListener
import com.project.navermap.presentation.mainActivity.map.mapFragment.navermap.MarkerFactory
import com.project.navermap.presentation.mainActivity.map.mapFragment.navermap.NaverMapHandler
import com.project.navermap.presentation.mainActivity.store.restaurant.RestaurantCategory
import com.project.navermap.presentation.base.BaseFragment
import com.project.navermap.util.provider.ResourcesProvider
import com.project.navermap.widget.adapter.ModelRecyclerAdapter
import com.project.navermap.widget.adapter.listener.MapItemListAdapterListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import javax.inject.Inject
import javax.inject.Provider

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>(), OnMapReadyCallback {

    private val viewModel: MapViewModel by viewModels()
    private val activityViewModel by activityViewModels<MainViewModel>()

    override fun getViewBinding(): FragmentMapBinding = FragmentMapBinding.inflate(layoutInflater)

    lateinit var naverMap: NaverMap

    @Inject lateinit var resourcesProvider: ResourcesProvider
    @Inject lateinit var markerFactory: MarkerFactory
    @Inject lateinit var naverMapHandlerProvider: Provider<NaverMapHandler>
    private val naverMapHandler get() = naverMapHandlerProvider.get()

    private val infoWindow by lazy {
        InfoWindow().apply {
            adapter = object : InfoWindow.DefaultTextAdapter(requireContext()) {
                override fun getText(infoWindow: InfoWindow): CharSequence {
                    // info window에 가게 이름이 뜨도록
                    return (infoWindow.marker?.tag as RestaurantModel).restaurantTitle
                }
            }
        }
    }

    private val markerClickListener: MarkerClickListener = {
        // 이전에 열려있는 info window를 닫음
        this@MapFragment.infoWindow.close()
        this@MapFragment.infoWindow.open(this)

        viewModel.loadRestaurantItems((this.tag as RestaurantModel).restaurantInfoId)
        // 여기서 오픈한 말풍선은 fbtnViewPager2를 클릭하면 제거
        binding.viewPager2.visibility = View.VISIBLE
        binding.fbtnCloseViewPager.visibility = View.VISIBLE
        true
    }

    private val destMarker: Marker by lazy {
        markerFactory.createDestMarker()
    }

    private val viewPagerAdapter by lazy {
        ModelRecyclerAdapter<FoodModel, MapViewModel>(
            emptyList(), viewModel, resourcesProvider,
            object : MapItemListAdapterListener {
                override fun onClickItem(foodModel: FoodModel) {
                    // TODO: start activity
                }
            }
        )
    }

    private lateinit var filterDialog: FilterDialog
    private val locationSource: FusedLocationSource by lazy {
        FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    private val GEOCODE_USER_INFO = "2b4e5d3d2f35dd584b398978c3aca53a"

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        private const val FAILED_TO_GET_RESTAURANT_LIST = "리스트를 불러오는데 실패했습니다."
        private const val INITIALIZING_CURRENT_LOCATION = "CurLocation 초기화 중"
        private const val INITIALIZING_DESTINATION_LOCATION = "DestLocation 초기화 중"
    }

//    private val locationListener: LocationListener by lazy {
//        LocationListener { location ->
//            activityViewModel.curLocation = location
//        }
//    }

    private fun mapObserveData() {
        viewModel.data.observe(viewLifecycleOwner) {
            when (it) {
                is MapState.Uninitialized -> {}
                is MapState.Loading -> {}
                is MapState.Success -> naverMapHandler.updateRestaurantMarkers(
                    it.restaurantInfoList,
                    markerClickListener
                )
                is MapState.Error -> showToast(FAILED_TO_GET_RESTAURANT_LIST)
            }
        }

        viewModel.items.observe(viewLifecycleOwner) {
            viewPagerAdapter.submitList(it)
        }

        activityViewModel.locationData.observe(viewLifecycleOwner) {
            when (it) {
                is MainState.Uninitialized -> Unit
                is MainState.Loading -> {
                }
                is MainState.Success -> onMainStateSuccess(it)
                is MainState.Error -> {
                }
            }
        }
    }

    private fun onMainStateSuccess(success: MainState.Success) {
        val latlng = success.mapSearchInfoEntity.locationLatLng
        viewModel.loadRestaurantList(RestaurantCategory.ALL, latlng)
        naverMapHandler.moveCameraTo(LatLng(latlng.latitude, latlng.longitude)) {
            showToast("위치를 불러오는 중입니다.")
        }

        activityViewModel.destLocation?.let {
            naverMapHandler.updateDestMarker(
                destMarker,
                LatLng(it.latitude, it.longitude)
            )
        }
    }

    override fun initState() {
        super.initState()
        //initMap()
        setupClickListeners()
        filterDialog = FilterDialog(requireActivity())
        filterDialog.initDialog(viewModel)
        binding.mapView.getMapAsync(this@MapFragment)
        binding.viewPager2.adapter = viewPagerAdapter
    }

    private fun setupClickListeners() = with(binding) {
        btnCurLocation.setOnClickListener {
            // TODO: 현재 위치 마커 구현
            activityViewModel.curLocation?.let {
                naverMapHandler.moveCameraTo(it) {
                    showToast(INITIALIZING_CURRENT_LOCATION)
                }
            }
        }

        btnDestLocation.setOnClickListener {
            activityViewModel.destLocation?.let {
                naverMapHandler.moveCameraTo(it) {
                    showToast(INITIALIZING_DESTINATION_LOCATION)
                }
                naverMapHandler.updateDestMarker(
                    destMarker,
                    LatLng(
                        it.latitude,
                        it.longitude
                    )
                )
            }
        }

        btnSearchAround.setOnClickListener {
            val state = viewModel.data.value
            Log.d("TAG", "setupClickListeners: $state")
            if (state is MapState.Success) {
                naverMapHandler.updateRestaurantMarkers(
                    state.restaurantInfoList,
                    markerClickListener
                )
            } else {
                showToast(FAILED_TO_GET_RESTAURANT_LIST)
            }
        }

        btnFilter.setOnClickListener {
            filterDialog.dialog = filterDialog.builder.show()
            viewModel.loadRestaurantList(
                // TODO: 카테고리 필터 적용
                RestaurantCategory.ALL,
                (activityViewModel.locationData.value as MainState.Success).mapSearchInfoEntity.locationLatLng
            )
        }

        btnCloseMarkers.setOnClickListener {
            viewPager2.visibility = View.GONE
            fbtnCloseViewPager.visibility = View.GONE
            naverMapHandler.deleteMarkers()
        }

        fbtnCloseViewPager.setOnClickListener {
            viewPager2.visibility = View.GONE
            fbtnCloseViewPager.visibility = View.GONE
            infoWindow.close()
        }
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map.apply {
            locationSource = this@MapFragment.locationSource //현재 위치값을 넘긴다
            locationTrackingMode = LocationTrackingMode.NoFollow
            uiSettings.isLocationButtonEnabled = true
            uiSettings.isScaleBarEnabled = true
            uiSettings.isCompassEnabled = true
        }
        mapObserveData()
    }

//    @SuppressLint("MissingPermission")
//    private fun initMap() = with(binding) {
//        locationSource = FusedLocationSource(this@MapFragment, LOCATION_PERMISSION_REQUEST_CODE)
//
//        val locationManager =
//            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
//
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
//    }

    //웹뷰 관련 코드들 제거 해야함
    private val startSearchActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {

                val bundle = result.data?.extras //인텐트로 보낸 extras를 받아옵니다.
                val str = bundle?.get(MainActivity.MY_LOCATION_KEY).toString()
                var asw: MapSearchInfoEntity?

                Log.d("SearchActivityForResult", str)

                // TODO: coroutine과 retrofit으로 바꾸기
                Thread {
                    val obj: URL
                    val address: String = URLEncoder.encode(str, "UTF-8")

                    obj = URL(Url.GEOCODE_URL + address)

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
                        showToast(asw.toString(), Toast.LENGTH_LONG)
                    }

                }.start()
            }
        }

    private fun init() = with(binding.webViewAddress) {
//        webViewAddress = // 메인 웹뷰
//        webViewLayout = // 웹뷰가 속한 레이아웃
//        공통 설정

        settings.apply {
            javaScriptEnabled = true// javaScript 허용으로 메인 페이지 띄움
            javaScriptCanOpenWindowsAutomatically = true//javaScript window.open 허용
            setSupportMultipleWindows(true)
        }

        addJavascriptInterface(AndroidBridge(), "TestApp")
        loadUrl("")
        webChromeClient = this@MapFragment.webChromeClient
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

                obj = URL(Url.GEOCODE_URL + address)

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
                    showToast(asw.toString(), Toast.LENGTH_LONG)
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
}