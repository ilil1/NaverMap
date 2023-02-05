package com.project.navermap.presentation.mainActivity.store.storeDetail.menu

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.project.navermap.R
import com.project.navermap.data.entity.restaurant.RestaurantEntity
import com.project.navermap.databinding.FragmentStoreMarketMenuBinding
import com.project.navermap.domain.model.FoodModel
import com.project.navermap.presentation.base.BaseFragment
import com.project.navermap.util.provider.ResourcesProvider
import com.project.navermap.widget.adapter.ModelRecyclerAdapter
import com.project.navermap.widget.adapter.listener.StoreDetailItemListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StoreMenuFragment : BaseFragment<FragmentStoreMarketMenuBinding>() {

    override fun getViewBinding() = FragmentStoreMarketMenuBinding.inflate(layoutInflater)

    @Inject
    lateinit var resourcesProvider: ResourcesProvider
    private val viewModel by viewModels<StoreMenuViewModel>()

    private val adapter by lazy {
        ModelRecyclerAdapter<FoodModel, StoreMenuViewModel>(
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
            adapter.submitList(it)//Data callback
        }
    }

    override fun initState() {
        val storeData = arguments?.getParcelable<RestaurantEntity>(SALE_LIST_KEY)!!
        viewModel.loadRestaurantItems(storeData.restaurantInfoId)//Data Request
        binding.restaurantRecyclerView.adapter = adapter

        super.initState()
    }

    companion object {
        const val SALE_LIST_KEY = "saleList"

        fun newInstance(menu: RestaurantEntity): StoreMenuFragment {
            val bundle = Bundle().apply {
                putParcelable(SALE_LIST_KEY, menu)
            }
            return StoreMenuFragment().apply {
                arguments = bundle
            }
        }
    }
}