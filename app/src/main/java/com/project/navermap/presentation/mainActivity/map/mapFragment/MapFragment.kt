package com.project.navermap.presentation.mainActivity.map.mapFragment


import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.project.navermap.R
import com.project.navermap.databinding.FragmentMapBinding
import com.project.navermap.domain.model.FoodModel
import com.project.navermap.domain.model.RestaurantModel
import com.project.navermap.extensions.showToast
import com.project.navermap.presentation.mainActivity.MainState
import com.project.navermap.presentation.mainActivity.map.mapFragment.navermap.MarkerClickListener
import com.project.navermap.presentation.mainActivity.map.mapFragment.navermap.MarkerFactory
import com.project.navermap.presentation.mainActivity.map.mapFragment.navermap.NaverMapHandler
import com.project.navermap.presentation.mainActivity.store.restaurant.RestaurantCategory
import com.project.navermap.presentation.base.BaseFragment
import com.project.navermap.presentation.base.UiState
import com.project.navermap.presentation.base.failOrNull
import com.project.navermap.presentation.base.successOrNull
import com.project.navermap.presentation.mainActivity.MainViewModel
import com.project.navermap.util.provider.ResourcesProvider
import com.project.navermap.widget.adapter.ModelRecyclerAdapter
import com.project.navermap.widget.adapter.listener.MapItemListAdapterListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>(), OnMapReadyCallback {

    override fun getViewBinding() = FragmentMapBinding.inflate(layoutInflater)

    private val viewModel: MapViewModel by viewModels()
    private val activityViewModel by activityViewModels<MainViewModel>()

    @Inject
    lateinit var resourcesProvider: ResourcesProvider

    @Inject
    lateinit var markerFactory: MarkerFactory

    //네이버지도 불러와지고 핸들러에 지도를 주입해준다
    @Inject
    lateinit var naverMapHandlerProvider: Provider<NaverMapHandler>
    private val naverMapHandler get() = naverMapHandlerProvider.get()

    lateinit var naverMap: NaverMap

    private val infoWindow by lazy {
        InfoWindow().apply {
            adapter = object : InfoWindow.DefaultTextAdapter(requireContext()) {
                override fun getText(infoWindow: InfoWindow): CharSequence {
                    return (infoWindow.marker?.tag as RestaurantModel).restaurantTitle
                }
            }
        }
    }

    private val destMarker: Marker by lazy {
        markerFactory.createDestMarker()
    }

    private val markerClickListener: MarkerClickListener = {

        this@MapFragment.infoWindow.close()
        this@MapFragment.infoWindow.open(this)

        viewModel.loadRestaurantItems((this.tag as RestaurantModel).restaurantInfoId)
        // 여기서 오픈한 말풍선은 fbtnViewPager2를 클릭하면 제거
        binding.viewPager2.visibility = View.VISIBLE
        binding.fbtnCloseViewPager.visibility = View.VISIBLE
        true
    }

    private val viewPagerAdapter by lazy {
        ModelRecyclerAdapter<FoodModel, MapViewModel>(
            emptyList(), viewModel, resourcesProvider,
            object : MapItemListAdapterListener {
                override fun onClickItem(foodModel: FoodModel) {
                    Toast.makeText(
                        context,
                        R.string.failed_get_restaurant_list,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }

    private lateinit var filterDialog: FilterDialog

    private val locationSource: FusedLocationSource by lazy {
        FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

//    private val locationListener: LocationListener by lazy {
//        LocationListener { location ->
//            activityViewModel.curLocation = location
//        }
//    }

    private fun observeStateData() {
        observeState()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.mapDataState.collect {

                uiState.value = it

                when (it) {
                    is UiState.Success -> {
                        it.successOrNull()?.let {
                            it.successOrNull()?.let {
                                naverMapHandler.updateRestaurantMarkers(
                                    it.first,
                                    markerClickListener
                                )
                            }
                        }
                    }
                    is UiState.Fail -> {
                        it.failOrNull()?.let {
                            it.failOrNull()?.let {
                                Toast.makeText(
                                    context,
                                    R.string.failed_get_restaurant_list,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun mapObserveData() {
        viewModel.data.observe(viewLifecycleOwner) {
            when (it) {
                is MapState.Uninitialized -> {}
                is MapState.Loading -> {}
                is MapState.Success -> naverMapHandler.updateRestaurantMarkers(
                    it.restaurantInfoList,
                    markerClickListener
                )
                is MapState.Error -> Toast.makeText(
                    context,
                    R.string.failed_get_restaurant_list,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        viewModel.items.observe(viewLifecycleOwner) {
            viewPagerAdapter.submitList(it)
        }

        activityViewModel.locationData.observe(viewLifecycleOwner) {
            when (it) {
                is MainState.Uninitialized -> Unit
                is MainState.Loading -> {}
                is MainState.Success -> {
                    onMainStateSuccess(it)
                    viewModel.destLocation = it.mapSearchInfoEntity.locationLatLng
                    filterDialog.initDialog(viewModel, it.mapSearchInfoEntity.locationLatLng)
                }
                is MainState.Error -> {}
            }
        }
    }

    private fun onMainStateSuccess(success: MainState.Success) {

        val location = success.mapSearchInfoEntity.locationLatLng
        viewModel.loadRestaurantList(RestaurantCategory.ALL, location)
        naverMapHandler.moveCameraTo(LatLng(location.latitude, location.longitude)) {
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
        observeStateData()
        setupClickListeners()
        //hilt로 바꿔야함
        filterDialog = FilterDialog(requireActivity())
        binding.mapView.getMapAsync(this@MapFragment)
        binding.viewPager2.adapter = viewPagerAdapter
    }

    private fun setupClickListeners() = with(binding) {
        btnCurLocation.setOnClickListener {
            // TODO: 현재 위치 마커 구현
            activityViewModel.curLocation?.let {
                naverMapHandler.moveCameraTo(it) { showToast("CurLocation 초기화 중") }
            }
        }

        btnDestLocation.setOnClickListener {
            activityViewModel.destLocation?.let {
                //naverMapHandler.moveCameraTo(it, { showToast("DestLocation 초기화 중") })
                naverMapHandler.moveCameraTo(it) { showToast("DestLocation 초기화 중") }
                naverMapHandler.updateDestMarker(
                    destMarker,
                    LatLng(it.latitude, it.longitude)
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
                Toast.makeText(
                    context,
                    R.string.failed_get_restaurant_list,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        btnFilter.setOnClickListener {
            try {
                filterDialog.dialog = filterDialog.builder.show()
            } catch (ex: Exception) {
                Toast.makeText(context, "초기화 중", Toast.LENGTH_SHORT).show()
            }
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
}