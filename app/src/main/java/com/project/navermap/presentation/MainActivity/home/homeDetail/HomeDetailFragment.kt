package com.project.navermap.presentation.MainActivity.home.homeDetail

import com.project.navermap.databinding.FragmentHomeDetailBinding
import com.project.navermap.presentation.MainActivity.store.storeDetail.StoreDetailViewModel
import com.project.navermap.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.Navigation
import com.project.navermap.R
import com.project.navermap.domain.usecase.StoreViewModel.StoreDetailResult
import com.project.navermap.util.provider.ResourcesProvider
import javax.inject.Inject

@AndroidEntryPoint
class HomeDetailFragment : BaseFragment<FragmentHomeDetailBinding>() {

    private val viewModel by viewModels<StoreDetailViewModel>()

    @Inject
    lateinit var resourcesProvider: ResourcesProvider


    override fun initViews() = with(binding) {
        super.initViews()

        back.setOnClickListener {
            move()
            backStack()
        }
    }

    private fun move() {
        view?.let { it1 ->
            Navigation.findNavController(it1)
                .navigate(R.id.action_homeDetailFragment_to_homeFragment)
        }
    }

    private fun backStack() {
        view?.let { it1 ->
            Navigation.findNavController(it1).popBackStack()
        }
    }


    override fun getViewBinding(): FragmentHomeDetailBinding =
        FragmentHomeDetailBinding.inflate(layoutInflater)

    override fun observeData() = with(viewModel) {

        items.observe(viewLifecycleOwner) {
            when (it) {
                is StoreDetailResult.Success -> {

                }
                is StoreDetailResult.Loading -> {

                }
                is StoreDetailResult.Uninitialized -> {

                }
            }
        }
    }
}