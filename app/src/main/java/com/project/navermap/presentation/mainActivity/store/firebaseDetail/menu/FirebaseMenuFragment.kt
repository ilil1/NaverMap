package com.project.navermap.presentation.mainActivity.store.firebaseDetail.menu

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.project.navermap.R
import com.project.navermap.databinding.FragmentFirebaseMenuBinding
import com.project.navermap.domain.model.FoodModel
import com.project.navermap.presentation.base.BaseFragment
import com.project.navermap.util.provider.ResourcesProvider
import com.project.navermap.widget.adapter.ModelRecyclerAdapter
import com.project.navermap.widget.adapter.listener.StoreDetailItemListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FirebaseMenuFragment : BaseFragment<FragmentFirebaseMenuBinding>() {

    override fun getViewBinding() = FragmentFirebaseMenuBinding.inflate(layoutInflater)

    @Inject
    lateinit var resourcesProvider: ResourcesProvider
    private val viewModel by viewModels<FirebaseMenuViewModel>()

    private val adapter by lazy {
        ModelRecyclerAdapter<FoodModel, FirebaseMenuViewModel>(
            listOf(), viewModel, resourcesProvider,
            object : StoreDetailItemListener {
                override fun onClickItem(foodModel: FoodModel) {
                    Toast.makeText(
                        context,
                        R.string.cannot_load_data,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }

    override fun observeData() = with(viewModel) {
        storeItem.observe(viewLifecycleOwner) {
            adapter.submitList(it.map { entity -> entity.toModel() })//Data callback
        }
    }

    override fun initState() {
        val marketId = arguments?.getString(MARKET_ID_KEY)!!
        viewModel.loadRestaurantItems(marketId)//Data Request
        binding.restaurantRecyclerView.adapter = adapter

        super.initState()
    }

    companion object {
        const val MARKET_ID_KEY = "saleList"

        fun newInstance(marketId: String): FirebaseMenuFragment {
            val bundle = Bundle().apply {
                putString(MARKET_ID_KEY, marketId)
            }
            return FirebaseMenuFragment().apply {
                arguments = bundle
            }
        }
    }
}