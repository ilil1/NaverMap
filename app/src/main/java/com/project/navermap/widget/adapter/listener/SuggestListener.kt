package com.project.navermap.widget.adapter.listener

import com.project.navermap.domain.model.SuggestItemModel


interface SuggestListener : AdapterListener  {

    fun onClickItem(model: SuggestItemModel)

}