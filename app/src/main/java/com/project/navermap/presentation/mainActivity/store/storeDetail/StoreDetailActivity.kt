package com.project.navermap.presentation.mainActivity.store.storeDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.material.tabs.TabLayoutMediator
import com.project.navermap.data.entity.restaurant.RestaurantEntity
import com.project.navermap.extensions.load
import com.project.navermap.databinding.ActivityStoreDetailBinding
import com.project.navermap.presentation.mainActivity.store.StoreFragment
import com.project.navermap.presentation.mainActivity.store.storeDetail.information.StoreInformFragment
import com.project.navermap.presentation.mainActivity.store.storeDetail.menu.StoreMenuFragment
import com.project.navermap.presentation.mainActivity.store.storeDetail.review.StoreReviewFragment
import com.project.navermap.util.provider.ResourcesProvider
import com.project.navermap.widget.StoreDetailFragmentPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StoreDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoreDetailBinding
    private lateinit var viewPagerAdapter: StoreDetailFragmentPagerAdapter

    @Inject
    lateinit var resourcesProvider: ResourcesProvider
    @Inject
    lateinit var viewModelFactory: StoreDetailViewModel.StoreDetailAssistedFactory

    /**
     * hilt를 활용한 런타임 주입인데 Hilt의 의존성 관리상 저장되는 위치가 달라서 생기는 문제가 있음.
     */
    val viewModel by viewModels<StoreDetailViewModel> {
        StoreDetailViewModel.provideFactory(
            viewModelFactory,
            restaurantEntity = intent.getParcelableExtra(StoreFragment.StoreFragment_KEY)!!
        )
    }

    private fun observeData() = with(binding) {
        viewModel.storeDetailResultLiveData.observe(this@StoreDetailActivity) {
            when (it) {
                is StoreDetailResult.Uninitialized -> {}
                is StoreDetailResult.Loading -> {
                    progressBar.isVisible = true
                }
                is StoreDetailResult.Success -> {
                    handleSuccess(it)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.fetchData()
        observeData()

        binding.back.setOnClickListener {
            finish()
        }
    }

    private fun initViewPager(state: RestaurantEntity) {

        viewPagerAdapter = StoreDetailFragmentPagerAdapter(
            this@StoreDetailActivity,
            listOf(
                StoreMenuFragment.newInstance(state),
                StoreInformFragment.newInstance(),
                StoreReviewFragment.newInstance(state)
            )
        )
        val storeMarketDetailCategory = StoreDetailCategory.values()

        binding.menuAndReviewViewPager.adapter = viewPagerAdapter

        TabLayoutMediator(
            binding.menuAndReviewTabLayout,
            binding.menuAndReviewViewPager
        ) { tab, position -> tab.setText(storeMarketDetailCategory[position].categoryNameId)
        }.attach()
    }

    private fun handleSuccess(state: StoreDetailResult.Success) = with(binding) {
        progressBar.isGone = true

        val restaurantEntity = state.restaurantEntity

        TownMarketMainTitleTextView.text = restaurantEntity.restaurantTitle
        TownMarketImage.load(restaurantEntity.restaurantImageUrl)

        if (::viewPagerAdapter.isInitialized.not()) {
            initViewPager(restaurantEntity)
        }
    }

    companion object {
        fun newIntent(context: Context, restaurantEntity: RestaurantEntity) =
            Intent(context, StoreDetailActivity::class.java).apply {
                putExtra(StoreFragment.StoreFragment_KEY, restaurantEntity)
            }
    }
}