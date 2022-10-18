package com.project.navermap.presentation.MainActivity.myinfo.terms

import androidx.navigation.Navigation
import com.project.navermap.R
import com.project.navermap.databinding.FragmentTermsBinding
import com.project.navermap.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TermsFragment : BaseFragment<FragmentTermsBinding>() {

    private fun backMove() {
        view?.let { it1 ->
            Navigation.findNavController(it1).navigate(R.id.action_termsFragment_to_myInfoFragment)
        }
        backStack()
    }

    override fun getViewBinding(): FragmentTermsBinding =
        FragmentTermsBinding.inflate(layoutInflater)

    override fun observeData()  {}

    override fun initViews() {

        binding.configurationLeft.setOnClickListener {
            backMove()
        }

    }

    private fun backStack() {
        view?.let { it1 ->
            Navigation.findNavController(it1).popBackStack()
        }
    }


}