package com.project.navermap.widget.adapter.viewholder

import androidx.lifecycle.ViewModel
import com.project.navermap.extensions.clear
import com.project.navermap.extensions.load
import com.project.navermap.databinding.ViewholderSuggestSeasonBinding
import com.project.navermap.domain.model.SuggestItemModel
import com.project.navermap.util.provider.ResourcesProvider
import com.project.navermap.widget.adapter.listener.AdapterListener
import com.project.navermap.widget.adapter.listener.SuggestListener


class SuggestViewHolder(
    private val binding: ViewholderSuggestSeasonBinding,
    viewModel: ViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<SuggestItemModel>(binding, viewModel, resourcesProvider) {
    override fun reset() = with(binding) {
        ImageView.clear()
    }

    override fun bindData(model: SuggestItemModel) {
        super.bindData(model)

        with(binding) {
            ImageView.load(model.marketImageUrl, 16f)
            name.text = model.marketName // data
        }
    }

    override fun bindViews(model: SuggestItemModel, listener: AdapterListener) = with(binding) {
        if (listener is SuggestListener) {
                root.setOnClickListener {
                    listener.onClickItem(model)
                }
        }
    }
}