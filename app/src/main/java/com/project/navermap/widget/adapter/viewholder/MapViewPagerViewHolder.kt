package com.project.navermap.widget.adapter.viewholder

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.viewpager2.widget.ViewPager2
import com.project.navermap.extensions.clear
import com.project.navermap.extensions.load
import com.project.navermap.databinding.ViewholderMapViewpagerBinding
import com.project.navermap.domain.model.FoodModel
import com.project.navermap.util.provider.ResourcesProvider
import com.project.navermap.widget.adapter.listener.AdapterListener
import com.project.navermap.widget.adapter.listener.MapItemListAdapterListener
import kotlin.math.abs
import kotlin.math.max

class MapViewPagerViewHolder(
    private val binding: ViewholderMapViewpagerBinding,
    viewModel: ViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<FoodModel>(binding, viewModel, resourcesProvider) {

    override fun reset() {
        binding.ivViewpagerProfile.clear()
    }

    override fun bindData(model: FoodModel) {
        super.bindData(model)

        with(binding) {
            ivViewpagerProfile.load(model.imageUrl)
            tvViewpagerTitle.text = model.title
            tvViewpagerBranch.text = "N/A"
//                market.branch.ifEmpty { "본점" }
            tvViewpagerPrice.text = model.price
//                resourcesProvider.getString(R.string.price_format, model.originalPrice)
            tvViewpagerDiscountRate.text = "20%"
//                resourcesProvider.getString(R.string.discount_percent_format, model.discountRate)
            tvViewpagerDiscountedPrice.text = "N/A"
//                resourcesProvider.getString(
//                R.string.price_format,
//                (model.originalPrice * (100.0f - model.discountRate) * 0.01f).roundToInt()
//            )
            tvViewpagerPage.text = "N/A"
//                "${adapterPosition + 1} / ${market.items.size}"
        }
    }

    override fun bindViews(model: FoodModel, listener: AdapterListener) {
        if (listener is MapItemListAdapterListener) {
            binding.root.setOnClickListener { listener.onClickItem(model) }
        }
    }

    companion object ZoomOutTransformer : ViewPager2.PageTransformer {

        private const val MIN_SCALE = 0.85f // 뷰가 몇퍼센트로 줄어들 것인지
        private const val MIN_ALPHA = 0.5f // 어두워지는 정도를 나타낸 듯 하다.

        override fun transformPage(view: View, position: Float) {
            view.apply {
                val pageWidth = width
                val pageHeight = height
                when {
                    position < -1 -> { // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        alpha = 0f
                    }

                    position <= 1 -> { // [-1,1]
                        // Modify the default slide transition to shrink the page as well
                        val scaleFactor = max(MIN_SCALE, 1 - abs(position))
                        val vertMargin = pageHeight * (1 - scaleFactor) / 2
                        val horzMargin = pageWidth * (1 - scaleFactor) / 2
                        translationX = if (position < 0) {
                            horzMargin - vertMargin / 2
                        } else {
                            horzMargin + vertMargin / 2
                        }

                        // Scale the page down (between MIN_SCALE and 1)
                        scaleX = scaleFactor
                        scaleY = scaleFactor

                        // Fade the page relative to its size.
                        alpha = (MIN_ALPHA +
                                (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                    }

                    else -> { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        alpha = 0f
                    }
                }
            }
        }
    }
}

