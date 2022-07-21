package com.project.navermap

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHost
import androidx.navigation.ui.setupWithNavController
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
import com.project.navermap.screen.map.mapLocationSetting.MapLocationSettingActivity
import com.project.navermap.screen.map.myLocation.MyLocationActivity
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {

    private lateinit var uiScope: CoroutineScope // 코루틴 생명주기 관리
    private lateinit var binding: ActivityMainBinding

    private lateinit var locationManager: LocationManager
    private lateinit var naverMap: NaverMap

    private lateinit var curLocation: Location

    private val GEOCODE_URL = "http://dapi.kakao.com/v2/local/search/address.json?query="
    private val GEOCODE_USER_INFO = "2b4e5d3d2f35dd584b398978c3aca53a"

    private lateinit var mapSearchInfoEntity: MapSearchInfoEntity

    companion object {

        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        private const val DISTANCE = 300
        const val MY_LOCATION_KEY = "MY_LOCATION_KEY"

        private val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    private lateinit var myLocationListener: MyLocationListener

    private val navController by lazy {
        val hostContainer =
            supportFragmentManager
                .findFragmentById(R.id.fragmentContainer)
                    as NavHost

        hostContainer.navController
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {

                val bundle = result.data?.extras
                val result = bundle?.get("result")

                Toast.makeText(this, result.toString(), Toast.LENGTH_LONG).show()
            }
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

                        binding.locationTitleTextView.text = "${body?.addressInfo?.fullAddress}"

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

    private val changeLocationLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { results ->
            results.data?.getParcelableExtra<MapSearchInfoEntity>(
                MapLocationSettingActivity.MY_LOCATION_KEY
            )
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

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val responsePermissions = permissions.entries.filter {
                it.key in PERMISSIONS
            }
            if (responsePermissions.filter { it.value == true }.size == PERMISSIONS.size) {
                setLocationListener()
            } else {
                Toast.makeText(this, "no", Toast.LENGTH_SHORT).show()
            }
        }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNav.setupWithNavController(navController)

        uiScope = CoroutineScope(Dispatchers.Main)
        getMyLocation()

        binding.locationTitleTextView.setOnClickListener {
            try {
                myLocationStartForResult.launch(
                    MyLocationActivity.newIntent(this, mapSearchInfoEntity)
                )
            } catch (ex: Exception) {
                Toast.makeText(this, "myLocation 초기화 중", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getMyLocation() {
        if (::locationManager.isInitialized.not()) {
            locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
        val isGpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (isGpsEnable) {
            permissionLauncher.launch(PERMISSIONS)
        }
    }

    @SuppressLint("MissingPermission")
    private fun initMap() {}

    @Suppress("MissingPermission")
    private fun setLocationListener() {
        val minTime: Long = 1000
        val minDistance = 1f

        if (::myLocationListener.isInitialized.not()) {
            myLocationListener = MyLocationListener()
        }

        with(locationManager) {
            requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime, minDistance, myLocationListener
            )

            requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                minTime, minDistance, myLocationListener
            )
        }
    }

    inner class MyLocationListener : LocationListener {
        override fun onLocationChanged(location: Location) {

            getReverseGeoInformation(
                LocationEntity(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
            )
            removeLocationListener()
        }

        @SuppressLint("MissingPermission")
        private fun removeLocationListener() {
            if (::locationManager.isInitialized && ::myLocationListener.isInitialized) {
                locationManager.removeUpdates(myLocationListener)
            }
        }
    }
}
