package com.project.navermap.presentation.mainActivity.myinfo.like

import com.google.android.material.tabs.TabLayoutMediator
import com.project.navermap.databinding.FragmentLikeBinding
import com.project.navermap.domain.model.category.LikeCategory
import com.project.navermap.presentation.base.BaseFragment
import com.project.navermap.widget.HomeListFragmentAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LikeFragment  : BaseFragment<FragmentLikeBinding>() {


    private lateinit var viewPagerAdapter: HomeListFragmentAdapter

    override fun getViewBinding(): FragmentLikeBinding =
        FragmentLikeBinding.inflate(layoutInflater)

    override fun initViews() {
        super.initViews()

    }



    override fun observeData() {

    }

}