package com.project.navermap.presentation.MainActivity.store.restaurant

import android.content.ContextWrapper
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.databinding.FragmentListBinding
import com.project.navermap.databinding.FragmentStoreBinding
import com.project.navermap.domain.model.RestaurantModel
import com.project.navermap.presentation.MainActivity.map.mapFragment.MapViewModel
import com.project.navermap.widget.adapter.ModelRecyclerAdapter
import com.project.navermap.widget.adapter.listener.RestaurantListListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RestaurantListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding

    private val restaurantCategory
    by lazy { arguments?.getSerializable(RESTAURANT_CATEGORY_KEY) as RestaurantCategory }

    val locationEntity
    by lazy<LocationEntity> { arguments?.getParcelable(LOCATION_KEY)!! }

    @Inject lateinit var ViewModelFactory: RestaurantListViewModel.RestaurantAssistedFactory

    val viewModel by viewModels<RestaurantListViewModel> {
        RestaurantListViewModel.provideFactory(ViewModelFactory, restaurantCategory, locationEntity)
    }

    private val adapter by lazy {
        ModelRecyclerAdapter<RestaurantModel, RestaurantListViewModel>(
            listOf(),
            viewModel,
            adapterListener = object : RestaurantListListener {
                override fun onClickItem(model: RestaurantModel) {
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(layoutInflater)
        observeData()
        viewModel.fetchData()
        return binding.root
    }


    fun observeData() {
        viewModel.restaurantListLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.recyclerVIew.adapter = adapter
        }
    }

    companion object {
        const val RESTAURANT_KEY = "Restaurant"
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
