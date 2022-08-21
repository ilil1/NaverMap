package com.project.navermap.widget.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.project.navermap.R
import com.project.navermap.databinding.SlideItemContainerBinding
import com.project.navermap.domain.model.SliderItemModel

class SliderAdater(
    private val sliderItems: MutableList<SliderItemModel>,
    private val viewPager2: ViewPager2
) : RecyclerView.Adapter<SliderAdater.SliderViewHolder>() {

    private val runnable = Runnable {
        sliderItems.addAll(sliderItems)
        notifyDataSetChanged()
    }

    inner class SliderViewHolder(val binding: SlideItemContainerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(Item: SliderItemModel) {
            binding.pagerimage.setImageResource(Item.image)
        }
    }

    override fun getItemCount(): Int = sliderItems.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val binding =
            SlideItemContainerBinding.inflate(LayoutInflater.from(parent.context),
                parent, false)
        return SliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(sliderItems[position])
        if (position == sliderItems.size - 2) {
            viewPager2.post(runnable)
        }
    }
}