package com.project.navermap.presentation.mainActivity.home.suggestItemDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.project.navermap.R
import com.project.navermap.data.entity.SuggestItemEntity
import com.project.navermap.databinding.ActivitySuggestItemDetailBinding
import com.project.navermap.domain.model.RestaurantModel
import com.project.navermap.extensions.load
import com.project.navermap.presentation.mainActivity.home.HomeFragment
import com.project.navermap.presentation.mainActivity.home.suggestItemDetail.menu.SuggestItemMenuFragment
import com.project.navermap.presentation.mainActivity.store.storeDetail.StoreDetailActivity
import com.project.navermap.util.provider.ResourcesProvider
import com.project.navermap.widget.StoreDetailFragmentPagerAdapter
import com.project.navermap.widget.adapter.ModelRecyclerAdapter
import com.project.navermap.widget.adapter.listener.RestaurantListListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SuggestItemDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySuggestItemDetailBinding
    private lateinit var viewPagerAdapter: StoreDetailFragmentPagerAdapter

    @Inject
    lateinit var resourcesProvider: ResourcesProvider
    @Inject
    lateinit var viewModelFactory: SuggestItemDetailViewModel.SuggestItemDetailAssistedFactory


    /**
     * hilt를 활용한 런타임 주입인데 Hilt의 의존성 관리상 저장되는 위치가 달라서 생기는 문제가 있음.
     */
    val viewModel by viewModels<SuggestItemDetailViewModel> {
        SuggestItemDetailViewModel.provideFactory(
            viewModelFactory,
            suggestItemEntity = intent.getParcelableExtra(HomeFragment.HomeFragment_KEY)!!
        )
    }

    private fun firstObserverData() = with(viewModel){
        homeListData.observe(this@SuggestItemDetailActivity){
            detailAdapter.submitList(it)

        }
    }


    private fun observeData() = with(binding) {
        viewModel.storeDetailResultLiveData.observe(this@SuggestItemDetailActivity) {
            when (it) {
                is SuggestItemDetailResult.Uninitialized -> {}
                is SuggestItemDetailResult.Loading -> {
                    //progressBar.isVisible = true
                }
                is SuggestItemDetailResult.Success -> {
                    handleSuccess(it)
                }
            }
        }
    }

    private fun handleSuccess(state: SuggestItemDetailResult.Success) = with(binding) {
        //progressBar.isGone = true

        val restaurantEntity = state.suggestItemEntity

        TownMarketMainTitleTextView.text = restaurantEntity.marketName
        TownMarketImage.load(restaurantEntity.marketImageUrl)

        if (::viewPagerAdapter.isInitialized.not()) {
            initViewPager(restaurantEntity)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuggestItemDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.fetchData()
        firstObserverData()
        viewModel.firstFetchData()
        observeData()

        binding.homeDetailRecyclerview.adapter = detailAdapter
        binding.homeDetailRecyclerview.layoutManager = LinearLayoutManager(this@SuggestItemDetailActivity)


//
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.main_frame, SuggestItemMenuFragment())
//            .commit()

        binding.back.setOnClickListener {
            finish()
        }
    }

    private fun initViewPager(state: SuggestItemEntity) {

//        viewPagerAdapter = StoreDetailFragmentPagerAdapter(
//            this@SuggestItemDetailActivity,
//            listOf(
//                SuggestItemMenuFragment.newInstance(state)
//                //SuggestItemReviewFragment.newInstance(state)
//            )
//        )
//        val storeMarketDetailCategory = SuggestItemDetailCategory.values()
//
//        binding.menuAndReviewViewPager.adapter = viewPagerAdapter
//
//        TabLayoutMediator(
//            binding.menuAndReviewTabLayout,
//            binding.menuAndReviewViewPager
//        ) { tab, position -> tab.setText(storeMarketDetailCategory[position].categoryNameId)
//        }.attach()
    }

    private val detailAdapter by lazy {
        ModelRecyclerAdapter<RestaurantModel, SuggestItemDetailViewModel>(
            listOf(), viewModel,resourcesProvider,
            adapterListener = object : RestaurantListListener{
                override fun onClickItem(model: RestaurantModel) {
                    startActivity(
                        StoreDetailActivity.newIntent(this@SuggestItemDetailActivity, model.toEntity())
                    )
                }

            }
        )
    }


    companion object {
        fun newIntent(context: Context, suggestItemEntity: SuggestItemEntity) =
            Intent(context, SuggestItemDetailActivity::class.java).apply {
                putExtra(HomeFragment.HomeFragment_KEY, suggestItemEntity)
            }
    }
}