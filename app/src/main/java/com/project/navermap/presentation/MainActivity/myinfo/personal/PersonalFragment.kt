package com.project.navermap.presentation.MainActivity.myinfo.personal

import androidx.navigation.Navigation
import com.project.navermap.R
import com.project.navermap.databinding.FragmentPersonalBinding
import com.project.navermap.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonalFragment  : BaseFragment<FragmentPersonalBinding>() {

    override fun getViewBinding(): FragmentPersonalBinding =
        FragmentPersonalBinding.inflate(layoutInflater)

    override fun observeData() = with(binding) {

    }

    override fun initViews() {

        binding.configurationLeft.setOnClickListener {
            backMove()

        }
    }

    private fun backMove() {
        view?.let { it1 ->
            Navigation.findNavController(it1)
                .navigate(R.id.action_personalFragment_to_myInfoFragment)
        }
        backStack()

    }

    private fun backStack() {
        view?.let { it1 ->
            Navigation.findNavController(it1).popBackStack()
        }
    }

}