package com.project.navermap.widget.adapter.listener

import com.project.navermap.domain.model.RestaurantModel

interface RestaurantListListener: AdapterListener {

    fun onClickItem(model: RestaurantModel)

}
