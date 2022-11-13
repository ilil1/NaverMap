package com.project.navermap.widget.adapter.listener

import com.project.navermap.domain.model.ItemModel

interface FirebaseItemListener : AdapterListener {
    fun onClickItem(foodModel: ItemModel)
}