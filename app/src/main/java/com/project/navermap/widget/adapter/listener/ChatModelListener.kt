package com.project.navermap.widget.adapter.listener

import com.project.navermap.domain.model.ChatModel

interface ChatModelListener: AdapterListener {
    fun onClickItem(listModel: ChatModel)
}