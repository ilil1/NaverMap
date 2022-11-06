package com.project.navermap.widget

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class StoreListFragmentPagerAdapter(
    fragment : Fragment,
) : FragmentStateAdapter(fragment) {

    var fragments : ArrayList<Fragment> = ArrayList()

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}