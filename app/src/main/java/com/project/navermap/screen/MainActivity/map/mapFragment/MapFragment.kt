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

    private val viewModel: MapViewModel by viewModels()
    private val activityViewModel by activityViewModels<MainViewModel>()

    private lateinit var binding: FragmentMapBinding

    private lateinit var filterDialog : FilterDialog

    private lateinit var locationSource: FusedLocationSource
    private lateinit var locationManager: LocationManager
    private lateinit var naverMap: NaverMap

    private val GEOCODE_URL = "http://dapi.kakao.com/v2/local/search/address.json?query="
    private val GEOCODE_USER_INFO = "2b4e5d3d2f35dd584b398978c3aca53a"

    companion object {

        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        private const val DISTANCE = 300
        const val MY_LOCATION_KEY = "MY_LOCATION_KEY"
    }

    private val locationListener: LocationListener by lazy {
        LocationListener { location ->
            activityViewModel.curLocation = location
        }
    }

    fun observeData() {
        viewModel.data.observe(viewLifecycleOwner) {
            when (it) {
                is MapState.Uninitialized -> {
                    viewModel.getApiShopList()
                }
                is MapState.Loading -> {}
                is MapState.Success -> {}
                is MapState.Error -> {}
            }
        }

        activityViewModel.locationData.observe(viewLifecycleOwner) {
            when (it) {
                is MainState.Uninitialized -> {}
                is MainState.Loading -> {}
                is MainState.Success -> {
                    viewModel.updateLocation(it.mapSearchInfoEntity.locationLatLng)
                    viewModel.removeAllMarkers()
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

        filterDialog = FilterDialog(requireActivity())
        filterDialog.initDialog(viewModel)
        initMap()
        observeData()

        binding.btnCurLocation.setOnClickListener {

            try {
                viewModel.getMap()?.cameraPosition = CameraPosition(
                    LatLng(activityViewModel.getCurrentLocation().latitude,
                        activityViewModel.getCurrentLocation().longitude),
                    15.0)

            } catch (ex: Exception) {
                Toast.makeText(context, "CurLocation ????????? ???", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(context, "DestLocation ????????? ???", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnSearchAround.setOnClickListener {

            try {
                viewModel.updateMarker(requireContext())
            } catch (ex: Exception) {
                Toast.makeText(requireContext(), "???????????? ???????????? ???", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnFilter.setOnClickListener {
            filterDialog.dialog = filterDialog.builder.show()
            //dialog.show()
        }

        binding.btnCloseMarkers.setOnClickListener {
            viewModel.removeAllMarkers()
        }

        binding.etSearch.setOnClickListener {
            init()
            startSearchActivityForResult.launch(
                Intent(requireContext(), SearchAddressActivity::class.java)
            )
        }

        return binding.root
    }

    private val startSearchActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->

            if (result.resultCode == AppCompatActivity.RESULT_OK) {

                val bundle = result.data?.extras //???????????? ?????? extras??? ???????????????.
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

    @SuppressLint("MissingPermission")
    private fun initMap() = with(binding) {

        locationSource = FusedLocationSource(this@MapFragment, LOCATION_PERMISSION_REQUEST_CODE)

        locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

//        locationManager.requestLocationUpdates(
//            LocationManager.GPS_PROVIDER,
//            1000,
//            1f,
//            locationListener)
//
//        locationManager.requestLocationUpdates(
//            LocationManager.NETWORK_PROVIDER,
//            1000,
//            1f,
//            locationListener)
    }

    private fun init() {

//        webViewAddress = // ?????? ??????
//        webViewLayout = // ????????? ?????? ????????????
//        ?????? ??????

        binding.webViewAddress.settings.run {
            javaScriptEnabled = true// javaScript ???????????? ?????? ????????? ??????
            javaScriptCanOpenWindowsAutomatically = true//javaScript window.open ??????
            setSupportMultipleWindows(true)
        }

        binding.webViewAddress.addJavascriptInterface(AndroidBridge(), "TestApp")
        binding.webViewAddress.loadUrl("")
        binding.webViewAddress.webChromeClient = webChromeClient
    }

    private inner class AndroidBridge {
        // ????????? JavaScript??? android ????????? ????????? ??? ????????? ?????????
        @JavascriptInterface
        fun setAddress(arg1: String?, arg2: String?, arg3: String?) {
            // search.php?????? callback ???????????? ??????

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

        /// ---------- ?????? ?????? ----------
        /// - ????????? JavaScript SDK??? ????????? ????????? popup??? ???????????????.
        /// - window.open() ?????? ??? ?????? ?????? webview??? ??????????????? ?????????.
        ///
        lateinit var dialog: Dialog

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreateWindow(
            view: WebView, isDialog: Boolean,
            isUserGesture: Boolean, resultMsg: Message
        ): Boolean {
            // ?????? ?????????
            var childWebView = WebView(view.context)
            Log.d("TAG", "?????? ?????????")
            // ?????? ????????? ???????????? ?????? ??????
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

            // TODO: ?????? ?????? ????????? onBackPressed() ??? ??????
            //       ???????????? ??????????????? ?????? ????????? ??????
            //       ?????? ?????? ????????? ?????????
            //   ex) childWebViewList.add(childWebView)

            // ?????? ??? ??????
            val transport = resultMsg.obj as WebView.WebViewTransport
            transport.webView = childWebView
            resultMsg.sendToTarget()

            return true
        }

        override fun onCloseWindow(window: WebView) {
            super.onCloseWindow(window)
            Log.d("?????? ", "onCloseWindow")
            dialog.dismiss()
            // ???????????? ????????????
            // TODO: ?????? ?????? ????????? onBackPressed() ??? ??????
            //       ???????????? ??????????????? ?????? ????????? ??????
            //       ?????? ?????? array ????????? ?????????
            //   ex) childWebViewList.remove(childWebView)
        }
    }

    override fun onMapReady(map: NaverMap) {

        this.naverMap = map.apply {
            this.locationSource = this@MapFragment.locationSource //?????? ???????????? ?????????
            locationTrackingMode = LocationTrackingMode.NoFollow
            uiSettings.isLocationButtonEnabled = true
            uiSettings.isScaleBarEnabled = true
            uiSettings.isCompassEnabled = true
        }

        viewModel.setMap(naverMap)

        try {//??????????????? ?????????????????? ?????? ?????????
            viewModel.firstupdateLocation()
        } catch (ex: Exception) {
            Toast.makeText(context, "?????? ????????? ???", Toast.LENGTH_SHORT).show()
        }
    }
}