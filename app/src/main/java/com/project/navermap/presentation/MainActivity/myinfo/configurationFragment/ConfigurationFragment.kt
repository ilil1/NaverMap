package com.project.navermap.presentation.MainActivity.myinfo.configurationFragment

import androidx.navigation.Navigation
import com.project.navermap.BuildConfig
import com.project.navermap.R
import com.project.navermap.databinding.FragmentConfigurationBinding
import com.project.navermap.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfigurationFragment: BaseFragment<FragmentConfigurationBinding>() {

    val versionNumber = BuildConfig.VERSION_NAME

    override fun getViewBinding(): FragmentConfigurationBinding =
        FragmentConfigurationBinding.inflate(layoutInflater)


    override fun observeData() = with(binding) {
        initViews()
    }

    private fun backMove() {
        view?.let { it1 ->
            Navigation.findNavController(it1)
                .navigate(R.id.action_configurationFragment_to_myInfoFragment)
        }
        backStack()


    }


    override fun initViews() = with(binding) {
        binding.versionCode.text = versionNumber
        binding.configurationLeft.setOnClickListener { backMove() }
    }

    private fun backStack() {
        view?.let { it1 ->
            Navigation.findNavController(it1).popBackStack()
        }
    }

}