package com.project.navermap.presentation.MainActivity.store.storeDetail.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.navermap.data.entity.restaurant.RestaurantEntity
import com.project.navermap.databinding.FragmentStoreMarketReviewBinding
import com.project.navermap.databinding.FragmentStoreMarketinformBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoreReviewFragment : Fragment() {

    private lateinit var binding : FragmentStoreMarketReviewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStoreMarketReviewBinding.inflate(layoutInflater)

        return binding.root
    }

    companion object {

        fun newInstance(menu : RestaurantEntity) : StoreReviewFragment {
            return StoreReviewFragment().apply {

            }
        }
    }

}