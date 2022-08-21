package com.project.navermap.presentation.MainActivity.store.storeDetail.menu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.navermap.data.entity.restaurant.RestaurantEntity
import com.project.navermap.databinding.FragmentStoreMarketMenuBinding
import com.project.navermap.domain.model.FoodModel
import com.project.navermap.domain.model.RestaurantModel
import com.project.navermap.presentation.MainActivity.store.StoreFragment
import com.project.navermap.presentation.MainActivity.store.storeDetail.StoreDetailViewModel
import com.project.navermap.presentation.MainActivity.store.storeDetail.review.StoreReviewFragment
import com.project.navermap.util.provider.ResourcesProvider
import com.project.navermap.widget.adapter.ModelRecyclerAdapter
import com.project.navermap.widget.adapter.listener.StoreDetailItemListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StoreMenuFragment : Fragment() {

    private lateinit var binding : FragmentStoreMarketMenuBinding

    @Inject
    lateinit var resourcesProvider: ResourcesProvider
    private val viewModel by viewModels<StoreMenuViewModel>()
   // val intent =  Intent()
  //  private val saleList = intent.getParcelableExtra<RestaurantEntity>("Store")

        private val adapter by lazy {
        ModelRecyclerAdapter<FoodModel, StoreMenuViewModel>(
            listOf(), viewModel, resourcesProvider,
            object : StoreDetailItemListener {
                override fun onClickItem(foodModel: FoodModel) {
                    TODO("Not yet implemented")
                }
            }
        )
    }

    private fun fetchData(restaurantEntity: RestaurantEntity){
        viewModel.items.observe(viewLifecycleOwner){
            when(it){
                is StoreMenuState.Uninitialized ->{
                    observeData()

                }
            }
        }
    }


    fun observeData() = viewModel.storeItem.observe(viewLifecycleOwner) {
        lifecycleScope.launch {
            adapter.submitList(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStoreMarketMenuBinding.inflate(layoutInflater)
        val storeData = arguments?.getParcelable<RestaurantEntity>(SALE_LIST_KEY)!!
        fetchData(storeData)
        //observeData(storeData)
        viewModel.loadRestaurantItems(storeData?.restaurantInfoId!!)
        binding.restaurantRecyclerView.adapter = adapter
        binding.restaurantRecyclerView.layoutManager = LinearLayoutManager(this@StoreMenuFragment.context)

        return binding.root
    }

    companion object {
        const val SALE_LIST_KEY = "saleList"

        fun newInstance(menu : RestaurantEntity) : StoreMenuFragment {
            val bundle = Bundle().apply {
                putParcelable(SALE_LIST_KEY, menu)
            }
            return StoreMenuFragment().apply {
                arguments = bundle
            }
        }
    }
}