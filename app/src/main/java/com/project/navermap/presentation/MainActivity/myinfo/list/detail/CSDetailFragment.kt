package com.project.navermap.presentation.mainActivity.myinfo.list.detail

import androidx.navigation.Navigation
import com.project.navermap.R
import com.project.navermap.databinding.FragmentDetailBinding
import com.project.navermap.domain.model.ImageData
import com.project.navermap.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CSDetailFragment  : BaseFragment<FragmentDetailBinding>() {


    override fun getViewBinding(): FragmentDetailBinding =
        FragmentDetailBinding.inflate(layoutInflater)

    override fun observeData() {

    }

    override fun initViews() = with(binding) {
        super.initViews()
        val csData = arguments?.getParcelable<ImageData>("data")
        title.text = csData?.csTitle.toString()
        contentTitle.text = csData?.csContentTitle.toString()
        contentBody.text = csData?.csContentBody.toString()


        uturn.setOnClickListener {
            changeFragment()
        }

    }

    private fun changeFragment() {
        view?.let { it1 ->
            Navigation.findNavController(it1)
                .navigate(R.id.action_CSDetailFragment_to_CSFragment)
        }
    }

    private fun backStack() {
        view?.let { it1 ->
            Navigation.findNavController(it1).popBackStack()
        }

    }


}
