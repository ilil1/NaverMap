package com.project.navermap.widget

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeListFragmentAdapter (
    activity : FragmentActivity,
    fragment: Fragment,
    private val fragmentList: List<Fragment>,
    // TODO latlngEntity
) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = fragmentList.size

    override fun createFragment(position: Int) = fragmentList[position]
}