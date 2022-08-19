package com.project.navermap.presentation.MainActivity

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.ui.setupWithNavController
import com.project.navermap.R
import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.data.entity.MapSearchInfoEntity
import com.project.navermap.databinding.ActivityMainBinding
import com.project.navermap.presentation.myLocation.MyLocationActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import javax.inject.Provider

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    companion object {

        const val MY_LOCATION_KEY = "MY_LOCATION_KEY"

        val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    private lateinit var myLocationListener: MyLocationListener

    @Inject
    lateinit var navControllerProvider: Provider<NavController>
    //fragment가 생성이되면 그때 그 id값을 반환하면서 get으로 가져오게된다.
    private val navController get() = navControllerProvider.get()

    private val changeLocationLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { results ->
            results.data?.getParcelableExtra<MapSearchInfoEntity>(MyLocationActivity.MY_LOCATION_KEY)
                ?.let { mapSearchInfoEntity ->
                    viewModel.getReverseGeoInformation(mapSearchInfoEntity.locationLatLng)
//                    viewModel.setDestinationLocation(mapSearchInfoEntity.locationLatLng)
                }
        }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
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
                is MainState.Uninitialized -> {
                    if (viewModel.getMyLocation(this@MainActivity)) {
                        permissionLauncher.launch(PERMISSIONS)
                    }
                }

                is MainState.Loading -> {}

                is MainState.Success -> {
                    locationLoading.isGone = true
                    locationTitleTextView.text = it.mapSearchInfoEntity.fullAddress
                }

                is MainState.Error -> {
                    locationTitleTextView.text = getString(it.errorMessage)
                }
            }
        }
    }

    @Suppress("MissingPermission")
    private fun setLocationListener() {
        val minTime: Long = 1000
        val minDistance = 1f

        if (::myLocationListener.isInitialized.not()) {
            myLocationListener = MyLocationListener()
        }

        with(viewModel.getLocationManager()) {
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
        @RequiresApi(Build.VERSION_CODES.P)
        override fun onLocationChanged(location: Location) {

//            viewModel.setCurrentLocation(location)
            viewModel.getReverseGeoInformation(
                LocationEntity(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
            )
            removeLocationListener()
        }

        @RequiresApi(Build.VERSION_CODES.P)
        @SuppressLint("MissingPermission")
        private fun removeLocationListener() {
            if (viewModel.getLocationManager().isLocationEnabled && ::myLocationListener.isInitialized) {
                viewModel.getLocationManager().removeUpdates(myLocationListener)
            }
        }
    }
}
