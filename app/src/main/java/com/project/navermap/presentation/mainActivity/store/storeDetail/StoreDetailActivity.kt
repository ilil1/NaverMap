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
import com.project.navermap.data.extensions.load
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

    private lateinit var viewPagerAdapter: StoreDetailFragmentPagerAdapter
    private lateinit var binding: ActivityStoreDetailBinding

    @Inject
    lateinit var resourcesProvider: ResourcesProvider
    @Inject lateinit var viewModelFactory: StoreDetailViewModel.StoreDetailAssistedFactory

    /**
     * hilt를 활용한 런타임 주입인데 Hilt의 의존성 관리상 저장되는 위치가 달라서 생기는 문제가 있음.
     */
    val viewModel by viewModels<StoreDetailViewModel> {
        StoreDetailViewModel.provideFactory(viewModelFactory
            ,restaurantEntity = intent.getParcelableExtra(StoreFragment.StoreFragment_KEY)!!)
    }

    private fun observeData() {
        viewModel.storeDetailResultLiveData.observe(this) {
            when(it){
                is StoreDetailResult.Uninitialized -> {}
                is StoreDetailResult.Loading -> {}
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

    private fun bundleData(restaurantEntity: RestaurantEntity) {
        initViewPager(restaurantEntity)
    }

    private fun initViewPager(state: RestaurantEntity) {
        viewPagerAdapter = StoreDetailFragmentPagerAdapter(
            this@StoreDetailActivity,
            listOf(
                StoreMenuFragment.newInstance(state),
                StoreInformFragment.newInstance(state),
                StoreReviewFragment.newInstance(state)
            )
        )
        val storeMarketDetailCategory = StoreDetailCategory.values()

        binding.menuAndReviewViewPager.adapter = viewPagerAdapter
        TabLayoutMediator(
            binding.menuAndReviewTabLayout,
            binding.menuAndReviewViewPager
        ) { tab, position ->
            tab.setText(storeMarketDetailCategory[position].categoryNameId)
        }.attach()

    }

    private fun handleLoading() = with(binding) {
        progressBar.isVisible = true
    }

    private fun handleSuccess(state: StoreDetailResult.Success) = with(binding) {
        progressBar.isGone = true

        val restaurantEntity = state.restaurantEntity

        val townMarketEntity = RestaurantEntity(
            id = restaurantEntity.id,
            restaurantInfoId = restaurantEntity.restaurantInfoId,
            restaurantCategory = restaurantEntity.restaurantCategory,
            restaurantTitle = restaurantEntity.restaurantTitle,
            restaurantImageUrl = restaurantEntity.restaurantImageUrl,
            restaurantTelNumber = restaurantEntity.restaurantTelNumber,
            grade = restaurantEntity.grade,
            reviewCount = restaurantEntity.reviewCount,
            deliveryTimeRange = restaurantEntity.deliveryTimeRange,
            deliveryTipRange = restaurantEntity.deliveryTipRange,
            latitude = restaurantEntity.latitude,
            longitude = restaurantEntity.longitude,
            isMarketOpen = restaurantEntity.isMarketOpen,
            distance= restaurantEntity.distance,
        )

        TownMarketMainTitleTextView.text = townMarketEntity.restaurantTitle
        TownMarketImage.load(restaurantEntity.restaurantImageUrl)

        if (::viewPagerAdapter.isInitialized.not()) {
            initViewPager(townMarketEntity)
        }
    }

    companion object {
        fun newIntent(context: Context, restaurantEntity: RestaurantEntity) =
            Intent(context, StoreDetailActivity::class.java).apply {
                putExtra(StoreFragment.StoreFragment_KEY, restaurantEntity)
            }
    }
}