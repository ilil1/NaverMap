package com.project.navermap.widget.adapter.listener

import com.project.navermap.domain.model.FoodModel

interface MapItemListAdapterListener : AdapterListener {
    fun onClickItem(foodModel: FoodModel)
}