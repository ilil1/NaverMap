package com.project.navermap.presentation.mainActivity.store.restaurant

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.databinding.FragmentListBinding
import com.project.navermap.domain.model.RestaurantModel
import com.project.navermap.presentation.mainActivity.store.storeDetail.StoreDetailActivity
import com.project.navermap.presentation.base.BaseFragment
import com.project.navermap.util.provider.ResourcesProvider
import com.project.navermap.widget.adapter.ModelRecyclerAdapter
import com.project.navermap.widget.adapter.listener.RestaurantListListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RestaurantListFragment : BaseFragment<FragmentListBinding>() {

    override fun getViewBinding(): FragmentListBinding =
        FragmentListBinding.inflate(layoutInflater)

    private val restaurantCategory
    by lazy { arguments?.getSerializable(RESTAURANT_CATEGORY_KEY) as RestaurantCategory }

    val locationEntity
    by lazy<LocationEntity> { arguments?.getParcelable(LOCATION_KEY)!! }

    @Inject lateinit var ViewModelFactory: RestaurantListViewModel.RestaurantAssistedFactory

    /**
     * hilt를 활용한 런타임 주입인데 Hilt의 의존성 관리상 저장되는 위치가 달라서 생기는 문제가 있음.
     */
    val viewModel by viewModels<RestaurantListViewModel> {
        RestaurantListViewModel.provideFactory(ViewModelFactory, restaurantCategory, locationEntity)
    }

    @Inject
    lateinit var resourcesProvider: ResourcesProvider

    private val adapter by lazy {
        ModelRecyclerAdapter<RestaurantModel, RestaurantListViewModel>(
            listOf(), viewModel, resourcesProvider,
            adapterListener = object : RestaurantListListener {
                override fun onClickItem(model: RestaurantModel) {
                    startActivity(
                        StoreDetailActivity.newIntent(requireContext(),model.toEntity())
                    )
                }
            })
    }

    override fun initState() {
        super.initState()
        viewModel.fetchData()
    }

    override fun observeData() {
        viewModel.restaurantListLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.recyclerView.adapter = adapter
        }
    }

    companion object {
        const val RESTAURANT_CATEGORY_KEY = "restaurantCategory"
        const val LOCATION_KEY = "location"

        fun newInstance(restaurantCategory: RestaurantCategory,
                        locationLatLng: LocationEntity): RestaurantListFragment {

            val bundle = Bundle().apply {
                putSerializable(RESTAURANT_CATEGORY_KEY, restaurantCategory)
                putParcelable(LOCATION_KEY, locationLatLng)
            }

            return RestaurantListFragment().apply {
                arguments = bundle
            }
        }
    }
}
