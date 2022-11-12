package com.project.navermap.presentation.mainActivity.store.firebaseDetail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.material.tabs.TabLayoutMediator
import com.project.navermap.data.entity.firebase.FirebaseEntity
import com.project.navermap.data.entity.restaurant.RestaurantEntity
import com.project.navermap.databinding.ActivityFirebaseDetailBinding
import com.project.navermap.databinding.ActivityStoreDetailBinding
import com.project.navermap.extensions.load
import com.project.navermap.presentation.mainActivity.store.StoreFragment
import com.project.navermap.presentation.mainActivity.store.restaurant.RestaurantListFragment
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
class FirebaseDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirebaseDetailBinding
    private lateinit var viewPagerAdapter: StoreDetailFragmentPagerAdapter

    @Inject
    lateinit var resourcesProvider: ResourcesProvider
    @Inject
    lateinit var viewModelFactory: FirebaseDetailViewModel.FirebaseDetailAssistedFactory

    /**
     * hilt를 활용한 런타임 주입인데 Hilt의 의존성 관리상 저장되는 위치가 달라서 생기는 문제가 있음.
     */
    val viewModel by viewModels<FirebaseDetailViewModel> {
        FirebaseDetailViewModel.provideFactory(
            viewModelFactory,
            firebaseEntity = intent.getParcelableExtra(RestaurantListFragment.RestaurantListFragment_KEY)!!
        )
    }

    private fun observeData() = with(binding) {
        viewModel.storeDetailResultLiveData.observe(this@FirebaseDetailActivity) {
            when (it) {
                is FirebaseDetailResult.Uninitialized -> {}
                is FirebaseDetailResult.Loading -> {
                    progressBar.isVisible = true
                }
                is FirebaseDetailResult.Success -> {
                    handleSuccess(it)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirebaseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.fetchData()
        observeData()

        binding.back.setOnClickListener {
            finish()
        }
    }

    private fun initViewPager(state: FirebaseEntity) {

        viewPagerAdapter = StoreDetailFragmentPagerAdapter(
            this@FirebaseDetailActivity,
            listOf(
                //StoreMenuFragment.newInstance(state),
                //StoreReviewFragment.newInstance(state)
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

    private fun handleSuccess(state: FirebaseDetailResult.Success) = with(binding) {
        progressBar.isGone = true

        val restaurantEntity = state.firebaseEntity

        TownMarketMainTitleTextView.text = restaurantEntity.restaurantTitle
        TownMarketImage.load(restaurantEntity.restaurantImageUrl)

        if (::viewPagerAdapter.isInitialized.not()) {
            initViewPager(restaurantEntity)
        }
    }

    companion object {
        fun newIntent(context: Context, firebaseEntity: FirebaseEntity) =
            Intent(context, FirebaseDetailActivity::class.java).apply {
                putExtra(RestaurantListFragment.RestaurantListFragment_KEY, firebaseEntity)
            }
    }
}