package com.project.navermap.presentation.MainActivity.myinfo.like

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
        if (!::viewPagerAdapter.isInitialized) initViewPager()
    }

    private fun initViewPager() = with(binding) {
        val likeCategories = LikeCategory.values()

        val likeListFragments = likeCategories.map {
            LikeListFragment.newInstance(it)
        }

        viewPagerAdapter = HomeListFragmentAdapter(
            requireActivity(),
            this@LikeFragment,
            fragmentList = likeListFragments
        )

        likeFragmentViewPager.adapter = viewPagerAdapter
        likeFragmentViewPager.offscreenPageLimit = likeCategories.size

        TabLayoutMediator(likeFragmentTabLayout, likeFragmentViewPager) { tab, position ->
            tab.setText(likeCategories[position].likeCategoryId)
        }.attach()
    }

    override fun observeData() {

    }

}