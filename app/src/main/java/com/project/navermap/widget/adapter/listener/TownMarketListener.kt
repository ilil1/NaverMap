package com.project.navermap.widget.adapter.listener

import com.project.navermap.domain.model.TownMarketModel


interface TownMarketListener : AdapterListener {

    fun onClickItem(model: TownMarketModel)

}