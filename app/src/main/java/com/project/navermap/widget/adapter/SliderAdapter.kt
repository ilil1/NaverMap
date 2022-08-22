package com.project.navermap.widget.adapter

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.project.navermap.databinding.SlideItemContainerBinding
import com.project.navermap.domain.model.SliderItemModel

class SliderAdapter(
    private val sliderItems: MutableList<SliderItemModel>,
    private val viewPager2: ViewPager2
) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

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

        Log.d("sliderItems.size", sliderItems.size.toString())
        if (position == sliderItems.size - 1) {
            Log.d("runnablecall", sliderItems.size.toString())
            viewPager2.post(runnable)
        }
    }
}