package com.project.navermap.widget.adapter.listener


import com.project.navermap.domain.model.HomeItemModel

interface HomeItemListener : AdapterListener {

    fun onClickItem(Model: HomeItemModel)
}