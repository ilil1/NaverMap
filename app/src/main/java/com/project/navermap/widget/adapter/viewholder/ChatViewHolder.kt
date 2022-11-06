package com.project.navermap.widget.adapter.viewholder

import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.project.navermap.extensions.load
import com.project.navermap.databinding.ViewholderChatlistBinding
import com.project.navermap.domain.model.ChatModel
import com.project.navermap.util.provider.ResourcesProvider
import com.project.navermap.widget.adapter.listener.AdapterListener
import com.project.navermap.widget.adapter.listener.ChatModelListener


class ChatViewHolder(
    private val binding: ViewholderChatlistBinding,
    viewModel: ViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<ChatModel>(binding, viewModel, resourcesProvider) {

    override fun reset() {
        binding.storeImage.clear()
    }

    override fun bindData(listModel: ChatModel) {
        super.bindData(listModel)

        with(binding) {

            storeImage.load(listModel.ImageUrl)
            storeName.text = listModel.StoreName
            date.text = listModel.Data
            recentlyText.text = listModel.RecentlyText
        }


    }

    override fun bindViews(listModel: ChatModel, listener: AdapterListener) {
        if (listener is ChatModelListener) {

            binding.root.setOnClickListener {
                listener.onClickItem(listModel)
            }
        }
    }
}