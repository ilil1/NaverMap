package com.project.navermap.presentation.mainActivity.store

import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.databinding.FragmentStoreBinding
import com.project.navermap.presentation.mainActivity.MainState
import com.project.navermap.presentation.mainActivity.store.restaurant.RestaurantCategory
import com.project.navermap.presentation.base.BaseFragment
import com.project.navermap.presentation.mainActivity.MainViewModel
import com.project.navermap.presentation.mainActivity.store.restaurant.RestaurantListFragment
import com.project.navermap.widget.RestaurantListFragmentPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoreFragment : BaseFragment<FragmentStoreBinding>() {

    override fun getViewBinding() = FragmentStoreBinding.inflate(layoutInflater)

    private val activityViewModel by activityViewModels<MainViewModel>()
    private lateinit var viewPagerAdapter: RestaurantListFragmentPagerAdapter

    override fun observeData() {
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
                locationLatLng,
            )

            viewPager.adapter = viewPagerAdapter
            viewPager.offscreenPageLimit = restaurantCategories.size

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.setText(RestaurantCategory.values()[position].categoryNameId)
            }.attach()
        }

        if (locationLatLng != viewPagerAdapter.locationLatLng) {
            viewPagerAdapter.locationLatLng = locationLatLng
            viewPagerAdapter.fragmentList.forEach {
                it.viewModel.setLocationLatLng(locationLatLng)
            }
        }
    }

    companion object {
        const val StoreFragment_KEY = "StoreFragment"
    }
}