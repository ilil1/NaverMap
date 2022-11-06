package com.project.navermap.widget.adapter.viewholder

import androidx.lifecycle.ViewModel
import com.project.navermap.R
import com.project.navermap.extensions.clear
import com.project.navermap.extensions.load
import com.project.navermap.databinding.ViewholderRestaurantBinding
import com.project.navermap.domain.model.RestaurantModel
import com.project.navermap.util.provider.ResourcesProvider
import com.project.navermap.widget.adapter.listener.AdapterListener
import com.project.navermap.widget.adapter.listener.RestaurantListListener

class RestaurantViewHolder(
    private val binding: ViewholderRestaurantBinding,
    viewModel: ViewModel,
    resourcesProvider: ResourcesProvider
): ModelViewHolder<RestaurantModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = with(binding) {
        restaurantImage.clear()
    }

    override fun bindData(model: RestaurantModel) {
        super.bindData(model)
        with(binding) {
            restaurantImage.load(model.restaurantImageUrl, 24f)
            restaurantTitleText.text = model.restaurantTitle
            gradeText.text = resourcesProvider.getString(R.string.grade_format, model.grade)
            reviewCountText.text = resourcesProvider.getString(R.string.review_count, model.reviewCount)
            val (minTime, maxTime) = model.deliveryTimeRange
            deliveryTimeText.text = resourcesProvider.getString(R.string.delivery_time, minTime, maxTime)

            val (minTip, maxTip) = model.deliveryTipRange
            deliveryTipText.text = resourcesProvider.getString(R.string.delivery_tip, minTip, maxTip)
        }
    }

    override fun bindViews(model: RestaurantModel, adapterListener: AdapterListener) = with(binding) {
        if (adapterListener is RestaurantListListener) {
            root.setOnClickListener {
                adapterListener.onClickItem(model)
            }
        }
    }

}
