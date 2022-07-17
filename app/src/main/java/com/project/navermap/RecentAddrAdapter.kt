package com.project.navermap

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.navermap.data.entity.AddressHistoryEntity
import com.project.navermap.databinding.RecyclerRecentAddrItemBinding

class RecentAddrAdapter(val itemClick: (AddressHistoryEntity) -> Unit) :
    RecyclerView.Adapter<RecentAddrAdapter.ViewHolder>() {

    var datas = mutableListOf<AddressHistoryEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val b = RecyclerRecentAddrItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(b, itemClick)
    }

    override fun getItemCount(): Int = datas.size

    fun clear() {
        datas.clear()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ViewHolder(
        private val binding: RecyclerRecentAddrItemBinding,
        itemClick: (AddressHistoryEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AddressHistoryEntity) {
            binding.tvAddr.text = item.name
            binding.root.setOnClickListener { itemClick(item) }
        }
    }
}