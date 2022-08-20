package com.project.navermap.domain.model

import androidx.recyclerview.widget.DiffUtil

abstract class Model(
    open val id : Long,
    open val type : CellType
) {

    open fun isTheSame(item: Model) =
        this.id == item.id && this.type == item.type

}