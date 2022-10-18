package com.project.navermap.widget.adapter.viewholder

import androidx.lifecycle.ViewModel
import com.project.navermap.extensions.clear
import com.project.navermap.extensions.load
import com.project.navermap.databinding.ViewholderNearbyMarketBinding
import com.project.navermap.domain.model.TownMarketModel
import com.project.navermap.util.provider.ResourcesProvider
import com.project.navermap.widget.adapter.listener.AdapterListener
import com.project.navermap.widget.adapter.listener.TownMarketListener


class NearbyMarketViewHolder(
    private val binding: ViewholderNearbyMarketBinding,
    viewModel: ViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<TownMarketModel>(binding, viewModel, resourcesProvider) {

    override fun reset() {
        // Extension을 이용하여 ImageView 초기화
        binding.nearbyMarketImageView.clear()
    }

    override fun bindViews(model: TownMarketModel, listener: AdapterListener) {
        if (listener is TownMarketListener) {
            binding.root.setOnClickListener {
                listener.onClickItem(model)
            }
        }
    }

    override fun bindData(model: TownMarketModel) {
        super.bindData(model)

        with(binding) {
            nearbyMarketTitle.text = model.marketName

            // 기본은 closed
            // Open일 경우에만 상태를 변경한다.
            //    if (model.isMarketOpen) {
            //          nearByMarketState.text = resourcesProvider.getString(R.string.market_open)
            //         nearByMarketState.setChipBackgroundColorResource(R.color.yellow)
            //     }

//            nearbyMarketDistance.text = String.format("%.1fkm", model.distance)
            //    nearbyMarketWorkDay.text = "연중무휴연중무휴연중무휴" // TODO remove hard coded

            // Extension을 사용하여 Glide로 ImageView에 이미지 로드
            nearbyMarketImageView.load(model.marketImageUrl)
        }
    }
}