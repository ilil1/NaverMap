package com.project.navermap.widget.adapter.viewholder

import androidx.lifecycle.ViewModel
import com.project.navermap.R
import com.project.navermap.extensions.clear
import com.project.navermap.extensions.load
import com.project.navermap.databinding.ViewholderTownMarketBinding
import com.project.navermap.domain.model.RestaurantModel
import com.project.navermap.domain.model.TownMarketModel
import com.project.navermap.util.provider.ResourcesProvider
import com.project.navermap.widget.adapter.listener.AdapterListener
import com.project.navermap.widget.adapter.listener.RestaurantListListener
import com.project.navermap.widget.adapter.listener.TownMarketListener

//백엔드 작업후 RestaurantModel을 TownMarketModel로 따로 분기해야함
class TownMarketViewHolder(
    private val binding: ViewholderTownMarketBinding,
    viewModel: ViewModel,
    resourcesProvider: ResourcesProvider
): ModelViewHolder<RestaurantModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = with(binding) {
        marketImageView.clear()
    }

    override fun bindData(model: RestaurantModel) {
        super.bindData(model)

        with(binding) {
            // TODO 실제 데이터를 받아오는 경우 데이터가 잘 반영이 되도록 수정
            marketImageView.load(model.restaurantImageUrl, 16f)
            marketNameText.text = model.restaurantTitle
            distanceTextView.text = model.distance.toString()+"km"
            stockTextView.text = "2개 상품 판매중"
            likeCountTextView.text = "1"
            reviewCountTextView.text = "1"
            likeTextView.text = resourcesProvider.getString(R.string.like)
            reviewTextView.text = resourcesProvider.getString(R.string.review)

            when(model.isMarketOpen) {
                true -> marketOpenStatusView.apply {
                    text = resourcesProvider.getString(R.string.market_open)
                    background = resourcesProvider.getDrawable(R.drawable.viewholder_town_market_open_shape)
                }

                false -> marketOpenStatusView.apply {
                    text = resourcesProvider.getString(R.string.market_closed)
                    background = resourcesProvider.getDrawable(R.drawable.viewholder_town_market_closed_shape)
                }
            }
        }
    }

    override fun bindViews(model: RestaurantModel, listener: AdapterListener) {
        if(listener is RestaurantListListener) {
            with(binding) {
                root.setOnClickListener {
                    listener.onClickItem(model)
                }
            }
        }
    }
}