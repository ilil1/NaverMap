package com.project.navermap.widget.adapter

import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.ListAdapter
import com.project.navermap.MapApplication
import com.project.navermap.domain.model.CellType
import com.project.navermap.domain.model.Model
import com.project.navermap.util.mapper.ViewHolderMapper
import com.project.navermap.util.provider.ResourcesProviderImpl
import com.project.navermap.util.provider.ResourcesProvider
import com.project.navermap.widget.adapter.listener.AdapterListener
import com.project.navermap.widget.adapter.viewholder.ModelViewHolder
import javax.inject.Inject


open class ModelRecyclerAdapter<M : Model, VM : ViewModel> (
    private var modelList: List<Model>,
    private val viewModel: VM,
    private val resourcesProvider: ResourcesProvider,
    private val adapterListener: AdapterListener
) : ListAdapter<Model, ModelViewHolder<M>>(Model.DIFF_CALLBACK) {

    override fun getItemViewType(position: Int): Int = modelList[position].type.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder<M>{
        return  ViewHolderMapper.map(parent, CellType.values()[viewType], viewModel, resourcesProvider)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: ModelViewHolder<M>, position: Int) {
        holder.bindData(modelList[position] as M)
        holder.bindViews(modelList[position] as M, adapterListener)

    }

    override fun submitList(list: List<Model>?) {
        list?.let { modelList = it }
        super.submitList(list)
    }
}