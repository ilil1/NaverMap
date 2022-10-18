package com.project.navermap.presentation.MainActivity.myinfo.cs

import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayoutMediator
import com.project.navermap.R
import com.project.navermap.databinding.FragmentCsBinding
import com.project.navermap.domain.model.category.CSCategory
import com.project.navermap.presentation.MainActivity.myinfo.list.CSListViewModel
import com.project.navermap.presentation.MainActivity.myinfo.list.CSListFragment
import com.project.navermap.presentation.base.BaseFragment
import com.project.navermap.widget.HomeListFragmentAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CsFragment : BaseFragment<FragmentCsBinding>() {

    private lateinit var viewAdapter : HomeListFragmentAdapter

    // private val args by navArgs<CSFragmentArgs>()

    private val viewModel by viewModels<CSListViewModel>()

    override fun getViewBinding(): FragmentCsBinding =
        FragmentCsBinding.inflate(layoutInflater)

    override fun observeData(){}



    private fun initviewPager() = with(binding){
        if (::viewAdapter.isInitialized.not()) {
            val csCategory = CSCategory.values()

            val CSListfragmnet = csCategory.map {
                CSListFragment.newInstance(it)
            }

            viewAdapter = HomeListFragmentAdapter(
                requireActivity(),
                this@CsFragment,
                CSListfragmnet
            )
            viewPagerCs.adapter = viewAdapter

            viewPagerCs.offscreenPageLimit = csCategory.size

            TabLayoutMediator(tabLayout,viewPagerCs){
                    tab, position -> tab.setText(csCategory[position].categoryNameId)
            }.attach()


        }
    }
    override fun initViews() = with(binding) {
        super.initViews()

        CSTextView.text = "자주하는 질문"
        initviewPager()


        editBtn.setOnClickListener {
            view?.let { it1 ->
                Navigation.findNavController(it1)
                    .navigate(R.id.action_CSFragment_to_emailFragment)
            }
        }
        csback.setOnClickListener {
            backMove()
            backStack()
        }

    }


    private fun doubleBackStack() {
        view?.let { it1 ->
            Navigation.findNavController(it1).popBackStack()
        }
        view?.let { it1 ->
            Navigation.findNavController(it1).popBackStack()
        }
    }

    private fun backMove() {
        view?.let { it1 ->
            Navigation.findNavController(it1)
                .navigate(R.id.action_CSFragment_to_CSCenterFragment)
        }
    }

    private fun backStack() {
        view?.let { it1 ->
            Navigation.findNavController(it1).popBackStack()
        }

    }


}