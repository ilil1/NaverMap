package com.project.navermap.widget.adapter.viewholder

import androidx.lifecycle.ViewModel
import com.project.navermap.databinding.ReviewItemBinding
import com.project.navermap.domain.model.RestaurantModel
import com.project.navermap.domain.model.ReviewModel
import com.project.navermap.util.provider.ResourcesProvider
import com.project.navermap.widget.adapter.listener.AdapterListener

class ReviewItemViewHolder(
    private val binding: ReviewItemBinding,
    viewModel: ViewModel,
    resourcesProvider: ResourcesProvider
): ModelViewHolder<ReviewModel>(binding, viewModel, resourcesProvider) {

    override fun reset() {

    }

    override fun bindData(model: ReviewModel) = with(binding) {

        reviewItemRatingbar.rating = model.rating.toFloat()
        tvTitle.text = model.title
        tvContent.text = model.content
        tvNickname.text = model.writerId.toString()

    }

    override fun bindViews(model: ReviewModel, listener: AdapterListener) {

    }
}