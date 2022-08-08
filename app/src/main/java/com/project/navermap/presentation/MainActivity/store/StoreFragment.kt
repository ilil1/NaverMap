package com.project.navermap.presentation.MainActivity.store

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.project.navermap.R
import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.databinding.FragmentMapBinding
import com.project.navermap.databinding.FragmentStoreBinding
import com.project.navermap.presentation.MainActivity.MainState
import com.project.navermap.presentation.MainActivity.MainViewModel
import com.project.navermap.presentation.MainActivity.store.restaurant.RestaurantCategory
import com.project.navermap.presentation.MainActivity.store.restaurant.RestaurantListFragment
import com.project.navermap.widget.RestaurantListFragmentPagerAdapter

class StoreFragment : Fragment() {

    private val activityViewModel by activityViewModels<MainViewModel>()
    private lateinit var viewPagerAdapter: RestaurantListFragmentPagerAdapter
    private lateinit var binding: FragmentStoreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentStoreBinding.inflate(layoutInflater)
        observeData()
        return binding.root
    }

    fun observeData() {
        activityViewModel.locationData.observe(viewLifecycleOwner) {
            when (it) {
                is MainState.Uninitialized -> {}
                is MainState.Loading -> {}
                is MainState.Success -> {
                    initViewPager(it.mapSearchInfoEntity.locationLatLng)
                }
                is MainState.Error -> {}
            }
        }
    }

    private fun initViewPager(locationLatLng: LocationEntity) = with(binding) {

        if (::viewPagerAdapter.isInitialized.not()) {

            val restaurantCategories = RestaurantCategory.values()

            val restaurantListFragmentList = restaurantCategories.map {
                RestaurantListFragment.newInstance(it, locationLatLng)
            }

            viewPagerAdapter = RestaurantListFragmentPagerAdapter(
                this@StoreFragment,
                restaurantListFragmentList,
                locationLatLng
            )

            viewPager.adapter = viewPagerAdapter
            viewPager.offscreenPageLimit = restaurantCategories.size

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.setText(RestaurantCategory.values()[position].categoryNameId)
            }.attach()
        }

//        if (locationLatLng != viewPagerAdapter.locationLatLng) {
//            viewPagerAdapter.locationLatLng = locationLatLng
//            viewPagerAdapter.fragmentList.forEach {
//                it.viewModel.setLocationLatLng(locationLatLng)
//            }
//        }
    }
}