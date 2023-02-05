package com.project.navermap.widget.adapter.listener

import com.project.navermap.domain.model.FirebaseModel
import com.project.navermap.domain.model.RestaurantModel

interface FirebaseListener: AdapterListener {
    fun onClickItem(model: FirebaseModel)
}