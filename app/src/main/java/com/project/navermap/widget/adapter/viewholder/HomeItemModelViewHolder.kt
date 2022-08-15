package com.project.navermap.widget.adapter.viewholder

import androidx.lifecycle.ViewModel
import com.project.navermap.R
import com.project.navermap.data.extensions.clear
import com.project.navermap.data.extensions.load
import com.project.navermap.databinding.ViewholderHomeItemBinding
import com.project.navermap.domain.model.HomeItemModel
import com.project.navermap.util.provider.ResourcesProvider
import com.project.navermap.widget.adapter.listener.AdapterListener
import com.project.navermap.widget.adapter.listener.HomeItemListener


class HomeItemModelViewHolder(
    private val binding: ViewholderHomeItemBinding,
    viewModel: ViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<HomeItemModel>(binding, viewModel, resourcesProvider) {


    override fun reset() = with(binding) {
        itemImageView.clear()
    }

    override fun bindData(model: HomeItemModel) {
        super.bindData(model)

        val disCountedPrice = model.originalPrice - model.salePrice
        val disCountPercent: Int = 100 * disCountedPrice / model.originalPrice

        with(binding) {
            itemImageView.load(model.itemImageUrl, 0f)
            itemDistanceTextView.text = "0.1"
            distanceUnitTextView.text =
                resourcesProvider.getString(R.string.distance_unit_kilometer)
            marketNameOfItemTextView.text = model.townMarketModel.marketName
            itemReviewCountTextView.text = model.reviewQuantity.toString()
            itemReviewTextView.text = resourcesProvider.getString(R.string.review)
            itemLikeCountTextView.text = model.likeQuantity.toString()
            //itemLikeTextView.text = resourcesProvider.getString(R.string.)

            itemNameTextView.text = model.itemName
            originPriceTextView.text =
                resourcesProvider.getString(R.string.home_item_price_format, model.originalPrice)
            disCountPercentTextView.text = resourcesProvider.getString(
                R.string.home_item_discount_percent_format,
                disCountPercent,
                "%"
            )
            salePriceTextView.text =
                resourcesProvider.getString(R.string.home_item_price_format, model.salePrice)
            itemStockTextView.text =
                resourcesProvider.getString(R.string.home_item_stock, model.stockQuantity)
        }
    }

    override fun bindViews(model: HomeItemModel, listener: AdapterListener) = with(binding) {
        if (listener is HomeItemListener) {
            root.setOnClickListener {
                listener.onClickItem(model)
            }
        }
    }
}