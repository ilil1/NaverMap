package com.project.navermap.widget.adapter.viewholder

import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.project.navermap.databinding.ReviewItemBinding
import com.project.navermap.databinding.ViewholderRestaurantBinding
import com.project.navermap.domain.model.RestaurantModel
import com.project.navermap.domain.model.ReviewModel
import com.project.navermap.util.provider.ResourcesProvider
import com.project.navermap.widget.adapter.listener.AdapterListener

class ReviewItemViewHolder(
    private val binding: ReviewItemBinding,
    viewModel: ViewModel,
    resourcesProvider: ResourcesProvider
): ModelViewHolder<RestaurantModel>(binding, viewModel, resourcesProvider) {

    override fun reset() {
        TODO("Not yet implemented")
    }

    fun bindData(data: ReviewModel) = with(binding) {

        reviewItemRatingbar.rating = data.rating.toFloat()
        tvTitle.text = data.title
        tvContent.text = data.content
        tvNickname.text = data.writerId.toString()

    }

    override fun bindViews(model: RestaurantModel, listener: AdapterListener) {
        TODO("Not yet implemented")
    }
}