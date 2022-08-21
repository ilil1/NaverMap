package com.project.navermap.widget.adapter.listener

import com.project.navermap.domain.model.FoodModel
import com.project.navermap.domain.model.RestaurantModel

interface StoreDetailItemListener : AdapterListener {
    fun onClickItem(foodModel: FoodModel)
}