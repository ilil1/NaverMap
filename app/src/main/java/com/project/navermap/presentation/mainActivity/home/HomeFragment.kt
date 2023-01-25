package com.project.navermap.presentation.mainActivity.home

import android.os.Handler
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.project.navermap.R
import com.project.navermap.databinding.FragmentHomeBinding
import com.project.navermap.domain.model.RestaurantModel
import com.project.navermap.domain.model.SliderItemModel
import com.project.navermap.domain.model.SuggestItemModel
import com.project.navermap.domain.model.TownMarketModel
import com.project.navermap.presentation.mainActivity.MainState
import com.project.navermap.presentation.mainActivity.MainViewModel
import com.project.navermap.presentation.base.BaseFragment
import com.project.navermap.presentation.mainActivity.home.suggestItemDetail.SuggestItemDetailActivity
import com.project.navermap.presentation.mainActivity.store.restaurant.RestaurantListViewModel
import com.project.navermap.presentation.mainActivity.store.storeDetail.StoreDetailActivity
import com.project.navermap.util.provider.ResourcesProvider
import com.project.navermap.widget.adapter.ModelRecyclerAdapter
import com.project.navermap.widget.adapter.SliderAdapter
import com.project.navermap.widget.adapter.listener.RestaurantListListener
import com.project.navermap.widget.adapter.listener.SuggestListener
import com.project.navermap.widget.adapter.listener.TownMarketListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override fun getViewBinding() = FragmentHomeBinding.inflate(layoutInflater)

    private lateinit var viewPager2: ViewPager2
    private val sliderHandler = Handler()

    private val viewModel: HomeViewModel by viewModels()
    private val activityViewModel by activityViewModels<MainViewModel>()

    @Inject
    lateinit var resourcesProvider: ResourcesProvider

    companion object {
        const val HomeFragment_KEY = "HomeFragment"
    }

    /**
     * Adpater를 따로 관리하는 클래스 구조 필요
     */
    private val suggestAdapter by lazy {
        ModelRecyclerAdapter<SuggestItemModel, HomeViewModel>(
            listOf(), viewModel, resourcesProvider,
            adapterListener = object : SuggestListener {
                override fun onClickItem(model: SuggestItemModel) {
//                    startActivity(
//                        SuggestItemDetailActivity.newIntent(requireContext(),model.toEntity())
//                    )
                    test()
                }
            }
        )
    }

    fun test() {
        Toast.makeText(requireContext(), "?", Toast.LENGTH_LONG).show()
    }

    private val annivalAdapter by lazy {
        ModelRecyclerAdapter<SuggestItemModel, HomeViewModel>(
            listOf(), viewModel, resourcesProvider,
            adapterListener = object : SuggestListener {
                override fun onClickItem(model: SuggestItemModel) {
//                    startActivity(
//                        SuggestItemDetailActivity.newIntent(requireContext(),model.toEntity())
//                    )
                }
            }
        )
    }

    private val seasonAdapter by lazy {
        ModelRecyclerAdapter<SuggestItemModel, HomeViewModel>(
            listOf(), viewModel, resourcesProvider,
            adapterListener = object : SuggestListener {
                override fun onClickItem(model: SuggestItemModel) {
//                    startActivity(
//                        SuggestItemDetailActivity.newIntent(requireContext(),model.toEntity())
//                    )
                }
            }
        )
    }

    override fun observeData() = with(viewModel) {

        suggestData.observe(viewLifecycleOwner) {
            when (it) {
                is HomeMainState.Uninitialized -> {}
                is HomeMainState.Loading -> {}
                is HomeMainState.ListLoaded -> {}
                is HomeMainState.Success<*> -> {
                    suggestAdapter.submitList(it.modelList)
                }
                is HomeMainState.Error -> {
                    Toast.makeText(
                        context,
                        R.string.cannot_load_data,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        seasonData.observe(viewLifecycleOwner) {
            when (it) {
                is HomeMainState.Uninitialized -> {}
                is HomeMainState.Loading -> {}
                is HomeMainState.ListLoaded -> {}
                is HomeMainState.Success<*> -> {
                    seasonAdapter.submitList(it.modelList)
                }

                is HomeMainState.Error -> {
                    Toast.makeText(
                        context,
                        R.string.cannot_load_data,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        annivalData.observe(viewLifecycleOwner) {
            when (it) {

                is HomeMainState.Uninitialized -> {}
                is HomeMainState.Loading -> {}
                is HomeMainState.ListLoaded -> {}
                is HomeMainState.Success<*> -> {
                    annivalAdapter.submitList(it.modelList)
                }
                is HomeMainState.Error -> {
                    Toast.makeText(
                        context,
                        R.string.cannot_load_data,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        activityViewModel.locationData.observe(viewLifecycleOwner) {
            if (it is MainState.Success) {
                viewModel.fetchData()
            }
        }
    }

    private val runnable = Runnable {
        viewPager2.currentItem = 0
    }

    private val sliderRunnable by lazy {
        Runnable {
            viewPager2.currentItem = viewPager2.currentItem + 1
        }
    }

    private val items by lazy {
        mutableListOf<SliderItemModel>().apply {
            for (i: Int in 1..4) {
                add(SliderItemModel(R.drawable.testimage3))
            }
        }
    }

    override fun initViews() {
        super.initViews()

        viewPager2 = binding.pager
        viewPager2.post(runnable) //0번 position으로 초기화
        viewPager2.getChildAt(0)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        viewPager2.adapter = SliderAdapter(items, viewPager2)

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 3000)
            }
        })

        with(binding) {
            //searchView.isSubmitButtonEnabled = true
            seasonRecycler.adapter = seasonAdapter
            popularRecycler.adapter = suggestAdapter
            annivalRecyclerView.adapter = annivalAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        //AVD 기준으로 Data 삭제후 처음 실행시 이렇게 onStart에서만 adapter을 선언하면 작동을 안함
        //그래서 initView에 추가했음
        viewPager2.adapter = SliderAdapter(items, viewPager2)
    }

    override fun onPause() {
        super.onPause()
        viewPager2.adapter = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewPager2.adapter = null
    }
}