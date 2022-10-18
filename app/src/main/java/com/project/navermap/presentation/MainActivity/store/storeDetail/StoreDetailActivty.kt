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
import com.project.navermap.domain.usecase.storeViewModel.StoreDetailResult
import com.project.navermap.presentation.mainActivity.store.StoreFragment
import com.project.navermap.presentation.mainActivity.store.storeDetail.information.StoreInformFragment
import com.project.navermap.presentation.mainActivity.store.storeDetail.menu.StoreMenuFragment
import com.project.navermap.presentation.mainActivity.store.storeDetail.review.StoreReviewFragment
import com.project.navermap.util.provider.ResourcesProvider
import com.project.navermap.widget.StoreDetailFragmentPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StoreDetailActivty : AppCompatActivity() {


    private lateinit var viewPagerAdapter: StoreDetailFragmentPagerAdapter
    private lateinit var binding: ActivityStoreDetailBinding

    @Inject
    lateinit var resourcesProvider: ResourcesProvider
    private val viewModel by viewModels<StoreDetailViewModel>()

    private fun observeData() {
        viewModel.items.observe(this){
            when(it){
                is StoreDetailResult.Success -> {

                }
                is StoreDetailResult.Loading -> {
                   // handleSuccess()
                }
                is StoreDetailResult.Uninitialized -> {
                    handleSuccess()
                }
            }
        }
    }

    lateinit var intentData: RestaurantEntity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intentData = intent.getParcelableExtra(StoreFragment.StoreFragment_KEY)!!
        observeData()
       // handleSuccess()
        //       bundleData(intentData)
//        var bundle = Bundle()
//        bundle.putParcelable("store",intentData)
//        StoreMenuFragment.newInstance(intentData)
//        viewModel.fetchData()

    }

    private fun bundleData(restaurantEntity: RestaurantEntity) {
//        val bundle = Bundle()
//        bundle.putParcelable("key",restaurantEntity)

//        val fragment = StoreMenuFragment()
//        fragment.arguments = bundle
        initViewPager(restaurantEntity)
    }

    private fun initViewPager(state: RestaurantEntity) {
        viewPagerAdapter = StoreDetailFragmentPagerAdapter(
            this@StoreDetailActivty,
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

    private fun handleSuccess() = with(binding) {
        progressBar.isGone = true
        val townMarketEntity = RestaurantEntity(
            id = intentData.id,
            restaurantInfoId = intentData.restaurantInfoId,
            restaurantCategory = intentData.restaurantCategory,
            restaurantTitle = intentData.restaurantTitle,
            restaurantImageUrl = intentData.restaurantImageUrl,
            restaurantTelNumber = intentData.restaurantTelNumber,
            grade = intentData.grade,
            reviewCount = intentData.reviewCount,
            deliveryTimeRange = intentData.deliveryTimeRange,
            deliveryTipRange = intentData.deliveryTipRange,
            latitude = intentData.latitude,
            longitude = intentData.longitude,
        )

        TownMarketMainTitleTextView.text = townMarketEntity.restaurantTitle
        TownMarketImage.load(intentData.restaurantImageUrl)
        if (::viewPagerAdapter.isInitialized.not()) {
            initViewPager(townMarketEntity)

        }


    }


    companion object {
        fun newIntent(context: Context, restaurantEntity: RestaurantEntity) =
            Intent(context, StoreDetailActivty::class.java).apply {
                putExtra(StoreFragment.StoreFragment_KEY, restaurantEntity)
            }
    }


}