package com.project.navermap.presentation.mainActivity.myinfo.like

import android.os.Bundle
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayoutMediator
import com.project.navermap.R
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

    override fun initViews() = with(binding){
        super.initViews()

        binding.back.setOnClickListener {
            backMove()
        }
    }

    private fun backMove() {
        view?.let { it1 ->
            Navigation.findNavController(it1)
                .navigate(R.id.action_likeFragment_to_myInfoFragment)
        }
    }
    private fun backStack() {
        view?.let { it1 ->
            Navigation.findNavController(it1).popBackStack()
        }
    }
    override fun observeData() {

    }

}