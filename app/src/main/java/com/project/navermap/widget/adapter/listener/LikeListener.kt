package com.project.navermap.widget.adapter.listener

import com.project.navermap.domain.model.Model

interface LikeListener : AdapterListener {
    fun onClick(model: Model)
    fun onDeleteClick(model: Model)
}