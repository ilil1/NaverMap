package com.project.navermap.screen.MainActivity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isGone
import androidx.navigation.NavHost
import androidx.navigation.ui.setupWithNavController
import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.data.entity.MapSearchInfoEntity
import com.project.navermap.R
import com.project.navermap.databinding.ActivityMainBinding
import com.project.navermap.screen.myLocation.MyLocationActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    private lateinit var locationManager: LocationManager

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

    private val changeLocationLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { results ->
            results.data?.getParcelableExtra<MapSearchInfoEntity>(MyLocationActivity.MY_LOCATION_KEY)?.let {
                    mapSearchInfoEntity ->
                    viewModel.getReverseGeoInformation(mapSearchInfoEntity.locationLatLng)
                    viewModel.setDestinationLocation(mapSearchInfoEntity.locationLatLng)
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
        binding.locationTitleTextView.setOnClickListener {

            viewModel.getMapSearchInfo()?.let { mapSearchInfoEntity ->
                changeLocationLauncher.launch(
                    MyLocationActivity.newIntent(this@MainActivity, mapSearchInfoEntity)
                )
            }
        }

        observeData()
    }

    private fun observeData() = with(binding) {

        viewModel.locationData.observe(this@MainActivity) {
            when (it) {
                is MainState.Uninitialized -> { getMyLocation() }

                is MainState.Loading -> {}

                is MainState.Success -> {
                    locationLoading.isGone = true
                    locationTitleTextView.text = it.mapSearchInfoEntity.fullAddress
                    viewModel.setDestinationLocation(it.mapSearchInfoEntity.locationLatLng)
                }//MainState.Success가 호출이 됬을 때

                is MainState.Error -> {
                    locationTitleTextView.text = getString(it.errorMessage)
                }
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

            viewModel.setCurrentLocation(location)
            viewModel.getReverseGeoInformation(
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
