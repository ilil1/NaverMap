package com.project.navermap.presentation.mainActivity.store

import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.databinding.FragmentListBinding
import com.project.navermap.presentation.base.BaseFragment
import com.project.navermap.presentation.mainActivity.store.restaurant.RestaurantCategory
import com.project.navermap.presentation.mainActivity.store.restaurant.RestaurantListFragment
import com.project.navermap.presentation.mainActivity.store.restaurant.StoreCategory
import dagger.hilt.android.AndroidEntryPoint

//@AndroidEntryPoint
//class HomeListFragment : BaseFragment<FragmentListBinding>() {
//
//
//    override fun getViewBinding(): FragmentListBinding = FragmentListBinding.inflate(layoutInflater)
//
//    private val restaurantCategory
//            by lazy { arguments?.getSerializable(RestaurantListFragment.RESTAURANT_CATEGORY_KEY) as RestaurantCategory }
//
//    private val storeCategory by lazy{arguments?.getSerializable(RestaurantListFragment.STORE_KEY) as StoreCategory }
//
//    val locationEntity
//            by lazy<LocationEntity> { arguments?.getParcelable(RestaurantListFragment.LOCATION_KEY)!! }
//
//
//
//}