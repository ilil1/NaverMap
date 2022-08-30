package com.project.navermap.presentation.mainActivity.store.storeDetail.information

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.navermap.data.entity.restaurant.RestaurantEntity
import com.project.navermap.databinding.FragmentStoreMarketMenuBinding
import com.project.navermap.databinding.FragmentStoreMarketinformBinding
import com.project.navermap.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoreInformFragment : BaseFragment<FragmentStoreMarketinformBinding>() {

    override fun getViewBinding() = FragmentStoreMarketinformBinding.inflate(layoutInflater)

    companion object {
        fun newInstance() : StoreInformFragment {
            return StoreInformFragment()
        }
    }
}