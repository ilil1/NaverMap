package com.project.navermap.widget.adapter.viewholder

import androidx.lifecycle.ViewModel
import com.project.navermap.databinding.ViewholderCsItemBinding
import com.project.navermap.domain.model.CSModel
import com.project.navermap.util.provider.ResourcesProvider
import com.project.navermap.widget.adapter.listener.AdapterListener
import com.project.navermap.widget.adapter.listener.CSModelListener

class CSViewHolder (
        private val binding: ViewholderCsItemBinding,
        viewModel: ViewModel,
        resourcesProvider: ResourcesProvider
    ) : ModelViewHolder<CSModel>(binding,viewModel,resourcesProvider){

        override fun reset() = Unit

        override fun bindData(listModel: CSModel) {
            super.bindData(listModel)
            with(binding) {
                questionText.text = listModel.csTitle

            }
        }


        override fun bindViews(listModel: CSModel, listener: AdapterListener) = with(binding){
            if (listener is CSModelListener) {
                binding.root.setOnClickListener {
                    listener.onClickItem(listModel)
                }
            }
        }
}