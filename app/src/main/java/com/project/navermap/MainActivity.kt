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
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons

class MainActivity : AppCompatActivity() , OnMapReadyCallback {

    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private var infoWindow: InfoWindow? = null

    companion object {

        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        private const val DISTANCE = 300

        private val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var mapFragment: MapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as MapFragment
        mapFragment.getMapAsync(this)

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    {
        Log.d("onRequest", "onRequestPermissionsResult")

        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated) { // 권한 거부됨
                Log.d("권한 거부", "권한 거부됨")
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            else {
                Log.d("권한 승인", "권한 승인됨")
                //naverMap.locationTrackingMode = LocationTrackingMode.Follow // 현위치 버튼 컨트롤 활성
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(map : NaverMap) {

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
}
