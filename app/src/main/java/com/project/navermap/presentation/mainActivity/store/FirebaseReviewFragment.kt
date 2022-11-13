package com.project.navermap.presentation.mainActivity.store

import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.project.navermap.data.entity.firebase.ReviewEntity
import com.project.navermap.databinding.FragmentStoreMarketReviewBinding
import com.project.navermap.domain.model.FirebaseModel
import com.project.navermap.domain.model.ReviewModel
import com.project.navermap.presentation.base.BaseFragment
import com.project.navermap.presentation.mainActivity.store.storeDetail.review.StoreReviewFragment
import com.project.navermap.util.provider.ResourcesProvider
import com.project.navermap.widget.adapter.ModelRecyclerAdapter
import com.project.navermap.widget.adapter.listener.FirebaseListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class FirebaseReviewFragment : BaseFragment<FragmentStoreMarketReviewBinding>() {
        override fun getViewBinding() = FragmentStoreMarketReviewBinding.inflate(layoutInflater)

    val viewModel by viewModels<StoreReviewViewModel>()

    @Inject
    lateinit var resourcesProvider: ResourcesProvider

    private val marketId by lazy {
        requireArguments().getString(KEY_MARKET_ID)!!
    }

    private val adapter by lazy {
        ModelRecyclerAdapter<ReviewModel, StoreReviewViewModel>(
            listOf(),viewModel,resourcesProvider,
            adapterListener = object : FirebaseListener{
                override fun onClickItem(model: FirebaseModel) {

                }
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initViews() {
        super.initViews()
        binding.ReviewRecyclerView.adapter = adapter
        viewModel.getReviewByMarketId(marketId)
//        viewModel.write("0","제목","내용",4)
    }

    override fun observeData() {
        viewModel.reviewData.observe(viewLifecycleOwner){
           Log.d("TAG","observer $it")
            adapter.submitList(it)
        }
    }

        companion object {
            private const val KEY_MARKET_ID = "MARKET_ID"

            fun newInstance(marketId : String) : FirebaseReviewFragment {
                return FirebaseReviewFragment().apply {
                    arguments = bundleOf(KEY_MARKET_ID to marketId)
                }
            }
        }
}