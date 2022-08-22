package com.project.navermap.presentation.MainActivity.home

import android.graphics.Typeface
import android.os.Handler
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.project.navermap.R
import com.project.navermap.databinding.FragmentHomeBinding
import com.project.navermap.domain.model.SliderItemModel
import com.project.navermap.domain.model.SuggestItemModel
import com.project.navermap.domain.model.TownMarketModel
import com.project.navermap.presentation.MainActivity.MainState
import com.project.navermap.presentation.MainActivity.MainViewModel
import com.project.navermap.presentation.base.BaseFragment
import com.project.navermap.util.provider.ResourcesProvider
import com.project.navermap.widget.adapter.ModelRecyclerAdapter
import com.project.navermap.widget.adapter.SliderAdapter
import com.project.navermap.widget.adapter.listener.SuggestListener
import com.project.navermap.widget.adapter.listener.TownMarketListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private lateinit var viewPager2: ViewPager2
    private val sliderHandler = Handler()

    private val viewModel: HomeViewModel by viewModels()
    private val activityViewModel by activityViewModels<MainViewModel>()

    override fun getViewBinding(): FragmentHomeBinding =
        FragmentHomeBinding.inflate(layoutInflater)

    @Inject
    lateinit var resourcesProvider: ResourcesProvider

    private val nearbyMarketAdapter by lazy {
        ModelRecyclerAdapter<TownMarketModel, HomeViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
             object : TownMarketListener {
                override fun onClickItem(model: TownMarketModel) {
                }
            }
        )
    }

    private val suggestAdapter by lazy {
        ModelRecyclerAdapter<SuggestItemModel, HomeViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            adapterListener = object : SuggestListener {
                override fun onClickItem(model: SuggestItemModel) {
                }
            }
        )
    }

    private val annivalAdapter by lazy {
        ModelRecyclerAdapter<SuggestItemModel, HomeViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            adapterListener = object : SuggestListener {
                override fun onClickItem(model: SuggestItemModel) {
                }
            }
        )
    }

    private val seasonAdapter by lazy {
        ModelRecyclerAdapter<SuggestItemModel, HomeViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            adapterListener = object : SuggestListener {
                override fun onClickItem(model: SuggestItemModel) {
                }
            }
        )
    }

    override fun observeData() = with(viewModel) {

        marketData.observe(viewLifecycleOwner) {
            when (it) {
                is HomeMainState.Uninitialized -> {}
                is HomeMainState.Loading -> {}
                is HomeMainState.Success<*> -> {
                    nearbyMarketAdapter.submitList(it.modelList)
                }
                is HomeMainState.Error -> {
                    Toast.makeText(
                        context,
                        R.string.cannot_load_data,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> Unit
            }
        }
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

    override fun initViews() {

        super.initViews()

        val slideritems = mutableListOf<SliderItemModel>().apply {
            for (i: Int in 1..4) {
                add(SliderItemModel(R.drawable.testimage3))
            }
        }

        val sliderRunnable = Runnable {
            viewPager2.currentItem = viewPager2.currentItem + 1
        }

        viewPager2 = binding.pager
        viewPager2.adapter = SliderAdapter(slideritems, viewPager2)
        viewPager2.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 3000)
            }
        })

        with(binding) {

            searchView.isSubmitButtonEnabled = true

            val spannable = SpannableStringBuilder(popular.text)
            spannable.setSpan(
                StyleSpan(Typeface.BOLD),
                10,
                13,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            popular.text = spannable

            // 근처 마켓 RecyclerView 설정
            nearbyMarketRecyclerView.adapter = nearbyMarketAdapter
            nearbyMarketRecyclerView.layoutManager = GridLayoutManager(
                requireContext(),
                1,
                GridLayoutManager.HORIZONTAL,
                false
            )

            // 인기있는 취미
            popularRecycler.adapter = suggestAdapter
            popularRecycler.layoutManager = GridLayoutManager(
                requireContext(),
                1,
                GridLayoutManager.HORIZONTAL,
                false
            )

            annivalRecyclerView.adapter = annivalAdapter
            annivalRecyclerView.layoutManager = GridLayoutManager(
                requireContext(),
                1,
                GridLayoutManager.HORIZONTAL,
                false
            )

            seasonRecycler.adapter = seasonAdapter
            seasonRecycler.layoutManager = GridLayoutManager(
                requireContext(),
                1,
                GridLayoutManager.HORIZONTAL,
                false
            )

//            showMoreTextView.setOnClickListener {
//                findNavController().navigate(
//                    HomeFragmentDirections.actionHomeMainFragmentToMap()
//                )
//            }
        }
    }
}