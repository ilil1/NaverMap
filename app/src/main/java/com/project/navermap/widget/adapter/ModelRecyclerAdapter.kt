package com.project.navermap.widget.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.project.navermap.domain.model.CellType
import com.project.navermap.domain.model.Model
import com.project.navermap.util.mapper.ViewHolderMapper
import com.project.navermap.util.provider.ResourcesProvider
import com.project.navermap.widget.adapter.listener.AdapterListener
import com.project.navermap.widget.adapter.viewholder.ModelViewHolder


open class ModelRecyclerAdapter<M : Model, VM : ViewModel> (
    private var modelList: List<Model>,
    private val viewModel: VM,
    private val resourcesProvider: ResourcesProvider,
    private val adapterListener: AdapterListener
) : ListAdapter<Model, ModelViewHolder<M>>(DIFF_CALLBACK) {

    override fun getItemViewType(position: Int): Int = modelList[position].type.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder<M> {
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

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Model>() {
            override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean {
                return oldItem.isTheSame(newItem)
            }
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
                return oldItem == newItem
            }
        }
    }
}

///**
// * [DIFF_CALLBACK]의 areContentsTheSame에서 사용할 equals
// * equals가 없으면 areContentsTheSame에 @SuppressLint("DiffUtilEquals")를 붙여야한다.
// * Model 클래스를 비교할 일이 없으므로 Annotation을 붙여도 상관없다.
// */
//override fun equals(other: Any?): Boolean {
//    if (this === other) return true
//    if (javaClass != other?.javaClass) return false
//
//    other as Model
//
//    if (id != other.id) return false
//    if (type != other.type) return false
//
//    return true
//}
//
//override fun hashCode(): Int {
//    var result = id.hashCode()
//    result = 31 * result + type.hashCode()
//    return result
//}

