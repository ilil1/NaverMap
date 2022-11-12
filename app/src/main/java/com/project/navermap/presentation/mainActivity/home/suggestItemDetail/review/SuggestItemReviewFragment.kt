package com.project.navermap.presentation.mainActivity.home.suggestItemDetail.review

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.navermap.R
import com.project.navermap.data.entity.SuggestItemEntity
import com.project.navermap.data.entity.restaurant.RestaurantEntity
import com.project.navermap.databinding.FragmentStoreMarketReviewBinding
import com.project.navermap.databinding.FragmentSuggestItemReviewBinding
import com.project.navermap.presentation.base.BaseFragment
import com.project.navermap.presentation.mainActivity.store.storeDetail.review.StoreReviewFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SuggestItemReviewFragment : BaseFragment<FragmentSuggestItemReviewBinding>() {

    override fun getViewBinding() = FragmentSuggestItemReviewBinding.inflate(layoutInflater)

    companion object {
        fun newInstance(menu : SuggestItemEntity) : SuggestItemReviewFragment {
            return SuggestItemReviewFragment()
        }
    }
}