package com.project.navermap.presentation.mainActivity.store.storeDetail.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.navermap.data.entity.restaurant.RestaurantEntity
import com.project.navermap.databinding.FragmentStoreMarketReviewBinding
import com.project.navermap.databinding.FragmentStoreMarketinformBinding
import com.project.navermap.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoreReviewFragment : BaseFragment<FragmentStoreMarketReviewBinding>() {

    override fun getViewBinding() = FragmentStoreMarketReviewBinding.inflate(layoutInflater)

    companion object {
        fun newInstance(menu : RestaurantEntity) : StoreReviewFragment {
            return StoreReviewFragment()
        }
    }

}