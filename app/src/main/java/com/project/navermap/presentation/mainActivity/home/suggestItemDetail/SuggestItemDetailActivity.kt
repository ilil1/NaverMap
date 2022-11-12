package com.project.navermap.presentation.mainActivity.home.suggestItemDetail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.material.tabs.TabLayoutMediator
import com.project.navermap.R
import com.project.navermap.data.entity.SuggestItemEntity
import com.project.navermap.data.entity.restaurant.RestaurantEntity
import com.project.navermap.databinding.ActivityStoreDetailBinding
import com.project.navermap.databinding.ActivitySuggestItemDetailBinding
import com.project.navermap.extensions.load
import com.project.navermap.presentation.mainActivity.home.HomeFragment
import com.project.navermap.presentation.mainActivity.home.suggestItemDetail.menu.SuggestItemMenuFragment
import com.project.navermap.presentation.mainActivity.home.suggestItemDetail.review.SuggestItemReviewFragment
import com.project.navermap.presentation.mainActivity.store.StoreFragment
import com.project.navermap.presentation.mainActivity.store.storeDetail.StoreDetailActivity
import com.project.navermap.presentation.mainActivity.store.storeDetail.StoreDetailCategory
import com.project.navermap.presentation.mainActivity.store.storeDetail.StoreDetailResult
import com.project.navermap.presentation.mainActivity.store.storeDetail.StoreDetailViewModel
import com.project.navermap.presentation.mainActivity.store.storeDetail.menu.StoreMenuFragment
import com.project.navermap.presentation.mainActivity.store.storeDetail.review.StoreReviewFragment
import com.project.navermap.util.provider.ResourcesProvider
import com.project.navermap.widget.StoreDetailFragmentPagerAdapter
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
        observeData()

        binding.back.setOnClickListener {
            finish()
        }
    }

    private fun initViewPager(state: SuggestItemEntity) {

        viewPagerAdapter = StoreDetailFragmentPagerAdapter(
            this@SuggestItemDetailActivity,
            listOf(
                SuggestItemMenuFragment.newInstance(state),
                SuggestItemReviewFragment.newInstance(state)
            )
        )
        val storeMarketDetailCategory = SuggestItemDetailCategory.values()

        binding.menuAndReviewViewPager.adapter = viewPagerAdapter

        TabLayoutMediator(
            binding.menuAndReviewTabLayout,
            binding.menuAndReviewViewPager
        ) { tab, position -> tab.setText(storeMarketDetailCategory[position].categoryNameId)
        }.attach()
    }


    companion object {
        fun newIntent(context: Context, suggestItemEntity: SuggestItemEntity) =
            Intent(context, SuggestItemDetailActivity::class.java).apply {
                putExtra(HomeFragment.HomeFragment_KEY, suggestItemEntity)
            }
    }
}