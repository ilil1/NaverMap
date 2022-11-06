package com.project.navermap.widget.adapter.viewholder

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.project.navermap.domain.model.Model
import com.project.navermap.util.provider.ResourcesProvider
import com.project.navermap.widget.adapter.listener.AdapterListener


abstract class ModelViewHolder<M : Model>(
    binding: ViewBinding,
    protected val viewModel: ViewModel,
    protected val resourcesProvider: ResourcesProvider
) : RecyclerView.ViewHolder(binding.root) {

    abstract fun reset()

    open fun bindData(model: M) {
        reset()
    }

    abstract fun bindViews(model: M, listener: AdapterListener)
}