package com.project.navermap.widget.adapter.listener

import com.project.navermap.domain.model.CSModel

interface CSModelListener: AdapterListener {
    fun onClickItem(listModel: CSModel)
}