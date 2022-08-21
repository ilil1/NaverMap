package com.project.navermap.widget.adapter.listener

import com.project.navermap.domain.model.RestaurantModel

interface StoreListener : AdapterListener {

    fun onClickItem(model: RestaurantModel)
}