package com.project.navermap.presentation.MainActivity.store.storeDetail.information

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.navermap.data.entity.restaurant.RestaurantEntity
import com.project.navermap.databinding.FragmentStoreMarketinformBinding
import com.project.navermap.presentation.MainActivity.store.storeDetail.review.StoreReviewFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoreInformFragment : Fragment() {

    private lateinit var binding: FragmentStoreMarketinformBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStoreMarketinformBinding.inflate(layoutInflater)
        return binding.root
    }

    companion object {

        fun newInstance(info : RestaurantEntity) : StoreInformFragment {
            return StoreInformFragment().apply {

            }
        }
    }
}